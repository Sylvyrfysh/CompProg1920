#ifndef C___NPJFILESYSTEM_H
#define C___NPJFILESYSTEM_H

#ifdef _MSC_VER

#include <filesystem>

namespace filesystem = std::filesystem;
#else

#include <functional>
#include <iostream>

#include <cstring>
#include <memory>
#include <string>
#include <sys/stat.h>
#include <vector>

#ifdef _WIN32
#include "dirent.h"
#else

#include <dirent.h>

#endif

namespace filesystem {
    class path {
    public:
#if defined(_WIN32) || defined(__CYGWIN__)
        static constexpr char preferred_separator = L'\\';
#else
        static constexpr char preferred_separator = L'/';
#endif
        path(const char* path_name);
        path(const path& other);
        path(const std::string& path_name);
        path filename() const;
        const char* c_str() const;
        const std::string& string() const;
        path& replace_filename(const path& filename);
        bool operator==(const path& rhs) const;
        path& operator/=(const path& rhs);
        path& operator/(const path& rhs);

    private:
        std::string path_name_;
        std::vector<size_t> separators_;
    };

    class recursive_directory_iterator;
    class directory_iterator;
    class directory_entry {
    public:
        explicit directory_entry(const filesystem::path& path);
        bool exists() const;
        bool is_directory() const;
        bool is_regular_file() const;
        bool is_symlink() const;
        const filesystem::path& path() const;
        bool operator==(const directory_entry& rhs) const;

    private:
        friend recursive_directory_iterator;
        friend directory_iterator;
        directory_entry(const filesystem::path& path, bool iterate);
        static int mode_to_type(decltype(stat::st_mode) mode);
        dirent* next();
        std::shared_ptr<DIR> dir_;
        std::shared_ptr<dirent> entry_;
        filesystem::path path_;
    };

// NOTE: follows links by default..
    class recursive_directory_iterator {
    public:
        explicit recursive_directory_iterator(const filesystem::path& path);
        recursive_directory_iterator() = default;
        const directory_entry& operator*() const;
        const directory_entry* operator->() const;
        recursive_directory_iterator& operator++();
        friend bool operator==(const recursive_directory_iterator& lhs,
                               const recursive_directory_iterator& rhs);
        friend bool operator!=(const recursive_directory_iterator& lhs,
                               const recursive_directory_iterator& rhs);

    private:
        std::vector<std::shared_ptr<directory_entry>> stack_;
    };

    inline bool operator==(const recursive_directory_iterator& lhs,
                           const recursive_directory_iterator& rhs) {
        // somewhat weak check but for practical purposes should be fine given inode is unique
        return lhs.stack_.size() == rhs.stack_.size() &&
               (lhs.stack_.empty() || *lhs.stack_.back() == *rhs.stack_.back());
    }

    inline bool operator!=(const recursive_directory_iterator& lhs,
                           const recursive_directory_iterator& rhs) {
        return !(lhs == rhs);
    }

    class directory_iterator {
    public:
        explicit directory_iterator(const filesystem::path& path);
        directory_iterator() = default;
        const directory_entry& operator*() const;
        const directory_entry* operator->() const;
        directory_iterator& operator++();
        friend bool operator==(const directory_iterator& lhs,
                               const directory_iterator& rhs);
        friend bool operator!=(const directory_iterator& lhs,
                               const directory_iterator& rhs);

    private:
        std::vector<std::shared_ptr<directory_entry>> stack_;
    };

    inline bool operator==(const directory_iterator& lhs,
                           const directory_iterator& rhs) {
        // somewhat weak check but for practical purposes should be fine given inode is unique
        return lhs.stack_.size() == rhs.stack_.size() &&
               (lhs.stack_.empty() || *lhs.stack_.back() == *rhs.stack_.back());
    }

    inline bool operator!=(const directory_iterator& lhs,
                           const directory_iterator& rhs) {
        return !(lhs == rhs);
    }

    inline bool exists(const path& p) {
        return directory_entry(p).exists();
    }

    inline bool is_directory(const path& p) {
        return directory_entry(p).is_directory();
    }
}

#endif

#endif //C___NPJFILESYSTEM_H
