#ifndef _MSC_VER
#include "NPJFilesystem.h"

#include <utility>
namespace filesystem {

    path::path(const char *path_name) : path(std::string(path_name)) {
    }

    path::path(const path &other) : path(other.path_name_) {
    }

    path::path(const std::string &path_name) : path_name_(path_name) { // NOLINT(modernize-pass-by-value)
        // TODO: squash repeated separators
        // delineate the path
        for (size_t npos = path_name_.find_first_of(preferred_separator); npos != std::string::npos;
             npos = path_name_.find_first_of(preferred_separator, npos + 1))
            separators_.push_back(npos);
    }

    path path::filename() const {
        if (separators_.empty())
            return *this;
        return path(path_name_.substr(separators_.back() + 1));
    }

    const char *path::c_str() const {
        return path_name_.c_str();
    }

    const std::string &path::string() const {
        return path_name_;
    }

    path &path::replace_filename(const path &filename) {
        // empty or just a file name then copy
        if (separators_.empty())
            return *this = filename;

        // TODO: if its just the root_name we replace that

        // take everything from the last separator
        path_name_.erase(separators_.back());
        separators_.pop_back();
        return *this /= filename;
    }

    bool path::operator==(const path &rhs) const {
        return path_name_ == rhs.path_name_;
    }

    path &path::operator/=(const path &rhs) {
        if (!rhs.path_name_.empty()) {
            // update the path
            auto len = path_name_.size();
            if (!path_name_.empty() && path_name_.back() != preferred_separator &&
                rhs.path_name_.front() != preferred_separator) {
                separators_.push_back(path_name_.size());
                path_name_.push_back(preferred_separator);
                ++len;
            }
            path_name_ += rhs.path_name_;
            // update the parts bookkeeping
            auto sep_len = separators_.size();
            separators_.insert(separators_.end(), rhs.separators_.begin(), rhs.separators_.end());
            for (auto i = separators_.begin() + sep_len; i != separators_.end(); ++i)
                (*i) += len;
        }
        return *this;
    }

    path &path::operator/(const path &rhs) {
        auto ret = new path(*this);
        if (!rhs.path_name_.empty()) {
            // update the path
            auto len = ret->path_name_.size();
            if (!ret->path_name_.empty() && ret->path_name_.back() != preferred_separator &&
                rhs.path_name_.front() != preferred_separator) {
                ret->separators_.push_back(ret->path_name_.size());
                ret->path_name_.push_back(preferred_separator);
                ++len;
            }
            ret->path_name_ += rhs.path_name_;
            // update the parts bookkeeping
            auto sep_len = ret->separators_.size();
            ret->separators_.insert(ret->separators_.end(), rhs.separators_.begin(), rhs.separators_.end());
            for (auto i = ret->separators_.begin() + sep_len; i != ret->separators_.end(); ++i)
                (*i) += len;
        }
        return *ret;
    }

    directory_entry::directory_entry(const filesystem::path &path) : directory_entry(path, false) {
    }

    bool directory_entry::exists() const {
        return entry_ != nullptr;
    }

    bool directory_entry::is_directory() const {
        return exists() && entry_->d_type == DT_DIR;
    }

    bool directory_entry::is_regular_file() const {
        return exists() && entry_->d_type == DT_REG;
    }

    bool directory_entry::is_symlink() const {
        return exists() && entry_->d_type == DT_LNK;
    }

    const filesystem::path &directory_entry::path() const {
        return path_;
    }

    bool directory_entry::operator==(const directory_entry &rhs) const {
        return (!entry_ && !rhs.entry_) || memcmp(entry_.get(), rhs.entry_.get(), sizeof(dirent)) == 0;
    }

    directory_entry::directory_entry(const filesystem::path &path, bool iterate)
            : dir_(nullptr), entry_(nullptr), path_(path) {
        // stat it first in case its not a directory
        struct stat s;
        if (stat(path_.c_str(), &s) == 0) {
            // if it is a directory and we are going to iterate over it
            if (S_ISDIR(s.st_mode) && iterate) {
                dir_.reset(opendir(path_.c_str()), [](DIR* d) { closedir(d); });
                return;
            }
            // make a dirent from stat info for starting out
            auto filename = path_.filename();
            entry_.reset(new dirent);
            // entry_->d_reclen =
            // entry_->d_off =
            entry_->d_ino = s.st_ino;
            strcpy(entry_->d_name, filename.c_str());
#ifdef _DIRENT_HAVE_D_NAMLEN
            entry_->d_namlen = filename.string().size();
#endif
            entry_->d_type = mode_to_type(s.st_mode);
        }
    }

    int directory_entry::mode_to_type(decltype(stat::st_mode) mode) {
        if (S_ISREG(mode))
            return DT_REG;
        else if (S_ISDIR(mode))
            return DT_DIR;
        else if (S_ISFIFO(mode))
            return DT_FIFO;
        else if (S_ISSOCK(mode))
            return DT_SOCK;
        else if (S_ISCHR(mode))
            return DT_CHR;
        else if (S_ISBLK(mode))
            return DT_BLK;
        else if (S_ISLNK(mode))
            return DT_LNK;
        else
            return DT_UNKNOWN;
    }

    dirent *directory_entry::next() {
        // if we can scan
        if (dir_) {
            bool first_entry = entry_ == nullptr;
            // we have to skip . and ..
            do {
                entry_.reset(readdir(dir_.get()), [](dirent*) {});
            } while (entry_ && (strcmp(entry_->d_name, ".") == 0 || strcmp(entry_->d_name, "..") == 0));
            // update the path
            if (entry_) {
                if (first_entry)
                    path_ /= entry_->d_name;
                else
                    path_.replace_filename(entry_->d_name);
                // fix the type if its unknown
                struct stat s;
                if (entry_->d_type == DT_UNKNOWN && stat(path_.c_str(), &s) == 0)
                    entry_->d_type = mode_to_type(s.st_mode);
            }
        }
        return entry_.get();
    }

    recursive_directory_iterator::recursive_directory_iterator(const filesystem::path &path) {
        stack_.emplace_back(new directory_entry(path, true));
        // if this was an iteratable directory go to the first entry
        if (stack_.back()->dir_)
            stack_.back()->next();
            // if we cant iterate then its an end itr
        else
            stack_.clear();
    }

    const directory_entry &recursive_directory_iterator::operator*() const {
        return *stack_.back();
    }

    const directory_entry *recursive_directory_iterator::operator->() const {
        return stack_.back().get();
    }

    recursive_directory_iterator &recursive_directory_iterator::operator++() {
        // if we have something to iterate
        if (!stack_.empty()) {
            // if its a directory we deepen our depth first search
            if (stack_.back()->is_directory())
                stack_.emplace_back(new directory_entry(stack_.back()->path_, true));
            // try to go horizontally and if not go up
            while (!stack_.empty() && !stack_.back()->next())
                stack_.pop_back();
        }
        return *this;
    }

    directory_iterator::directory_iterator(const filesystem::path &path) {
        stack_.emplace_back(new directory_entry(path, true));
        // if this was an iteratable directory go to the first entry
        if (stack_.back()->dir_)
            stack_.back()->next();
            // if we cant iterate then its an end itr
        else
            stack_.clear();
    }

    const directory_entry &directory_iterator::operator*() const {
        return *stack_.back();
    }

    const directory_entry *directory_iterator::operator->() const {
        return stack_.back().get();
    }

    directory_iterator &directory_iterator::operator++() {
        // if we have something to iterate
        if (!stack_.empty()) {
            // If it's a dir, add but do not recurse
            if (stack_.back()->is_directory())
                stack_.emplace_back(new directory_entry(stack_.back()->path_, false));
            // try to go horizontally and if not go up
            while (!stack_.empty() && !stack_.back()->next())
                stack_.pop_back();
        }
        return *this;
    }
}
#endif