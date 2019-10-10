const readline = require('readline');
const fs = require('fs');
​
const rl = readline.createInterface({
  input: fs.createReadStream('data.json')
});
​
rl.on('line', line => solve(JSON.parse(line)));
​
function reduceReviews(reviews) {
    let reduced = reviews.reduce((agg, cur) => {
        return {
            votes: agg.votes + cur.votes,
            points: agg.points + (cur.votes * cur.rating)
        };
    }, {votes: 0, points: 0});
    return reduced.points / reduced.votes;
}
​
function solve(friendsData) {
    let creators = {}, allReviews = [];
​
    friendsData.Seasons.forEach(season => season.Episodes.forEach(episode => {
        new Set([episode.Director, ...episode.Writers]).forEach(c => {
            if(creators[c] === undefined) {
                creators[c] = { name: c, reviews: [] };
            }
            creators[c].reviews.push({rating: episode.Rating, votes: episode.Reviews});
        });
​
        allReviews.push({rating: episode.Rating, votes: episode.Reviews});
    }));
​
    let outputData = [];
    Object.keys(creators).forEach(name => {
        let c = creators[name];
​
        c.rating = reduceReviews(c.reviews);
        outputData.push({name, rating: c.rating});
    });
​
    let averageRating = reduceReviews(allReviews);
    outputData.filter(o => o.rating > averageRating).sort((a, b) => {
        return b.rating - a.rating !== 0
            ? b.rating - a.rating
            : a.name.toLowerCase().localeCompare(b.name.toLowerCase());
    }).forEach(o => console.log(o.name, o.rating.toFixed(2)));
}