const readline = require('readline');
const fs = require('fs');

const rl = readline.createInterface({
    input: fs.createReadStream('data.json') //process.stdin
});

rl.on('line', (line) => {
	const json = JSON.parse(line);
	
	const directors = new Map();
	const writers = new Map();
	
	let totalRating = 0;
	let numOfRatings = 0;
	
	json.Seasons.forEach(season => season.Episodes.forEach(episode => {
        const episodeRating = episode.Rating * episode.Reviews;
		totalRating += episodeRating;
		numOfRatings += episode.Reviews;
		
		const director = episode.Director;
		if (directors.has(director)) {
			directors.get(director).push([episodeRating, episode.Reviews]);
		} else {
			directors.set(director, [[episodeRating, episode.Reviews]]);
		}
		
		episode.Writers.forEach(writer => {
			if (writers.has(writer)) {
				writers.get(writer).push([episodeRating, episode.Reviews]);
			} else {
				writers.set(writer, [[episodeRating, episode.Reviews]]);
			}
		});
    }));
	
	const finalMap = new Map();
	for (var [director, ratings] of directors.entries()) {
		let directorRatingSum = 0;
		let directorRatings = 0;
		ratings.forEach( rating => {
			directorRatingSum += rating[0];
			directorRatings += rating[1];
		});
		finalMap.set(director, (directorRatingSum / directorRatings).toFixed(2));
	}
	
	for (var [writer, ratings] of writers.entries()) {
		let writerRatingSum = 0;
		let writerRatings = 0;
		ratings.forEach( rating => {
			writerRatingSum += rating[0];
			writerRatings += rating[1];
		});
		finalMap.set(writer, (writerRatingSum / writerRatings).toFixed(2));
	}
	
	finalMap[Symbol.iterator] = function* () {
		yield* [...this.entries()].sort((a, b) => {
			const ratingDifference = b[1] - a[1];
			if (Math.abs(ratingDifference) < 10e-4) {
				return a[0].toLowerCase().localeCompare(b[0].toLowerCase());
			}
			return ratingDifference;
		});
	}
	
	const avgRating = totalRating / numOfRatings;
	
	for(const [person, rating] of [...finalMap]) {
		if (rating > avgRating) {
			console.log(person + " " + rating);
		}
	}
	
	rl.close();
});