let songs = [
  {
    name: "song 3",
    artist: "Jay",
    album: "Taipei",
    track: 3,
  },
  {
    name: "song 2",
    artist: "Alice",
    album: "Kyoto",
    track: 1,
  },
  {
    name: "song 1",
    artist: "Jay",
    album: "Taipei",
    track: 1,
  },
  {
    name: "song 4",
    artist: "Bob",
    album: "Radiohead",
    track: 1,
  },
];

function suffleAlbums(songs) {
  let albums = new Map();

  // Group songs by album by using Map
  for (let song of songs) {
    let albumName = song.album;
    albums.set(albumName, albums.has(albumName) ? albums.get(albumName).concat(song) : [song]);
  }

  // Sorting songs for each album
  for (let [albumName, songs] of albums) {
    songs.sort((a, b) => a.track - b.track);
  }

  // Generate random order for album groups
  return Array.from(albums).sort(() => Math.random() > 0.5 ? 1 : -1);
}

let results = suffleAlbums(songs);

for (let [album, songs] of results) {
  console.log(album, songs)
}
