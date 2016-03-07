package main
import (
	// "fmt"
	"github.com/wtolson/go-taglib"
	"strings"
	"os"
)
func main() {
	var artistList, albumList, songList, genreList []string
	musicDir := os.Getenv("HOME") + "/" + "Music"
	genreList, err := listDir(musicDir)
	if err != nil {
		panic(err)
	}
	for _, absGenre := range(genreList) {
		// fmt.Println("genre:\t" + absGenre)

		artistList, err = listDir(musicDir + "/" + absGenre)
		if err != nil {
			panic(err)
		}
		for _, absArtist := range(artistList) {
			// fmt.Println("artist:\t" + absArtist)
			songList, err = listMusic(musicDir, absGenre, absArtist, "")
			if err != nil {
				panic(err)
			}
			editMusic(musicDir, absGenre, absArtist, "", songList)
			albumList, err = listDir(musicDir + "/" + absGenre + "/" + absArtist)
			if err != nil {
				panic(err)
			}
			for _, absAlbum := range(albumList) {
				// fmt.Println("album:\t-\t" + absAlbum)
				songList, err = listMusic(musicDir, absGenre, absArtist, absAlbum)
				if err != nil {
					panic(err)
				}
				editMusic(musicDir, absGenre, absArtist, absAlbum, songList)
			}
		// fmt.Println()
		}
	}
}

func listDir(dir string) (subDirs []string, err error) {
	fileDir, err := os.Open(dir)
	if err != nil {
		return
	}
	files, err := fileDir.Readdir(-1)
	if err != nil {
		return
	}
	for _, file := range files {
		if file.IsDir() {
			subDirs = append(subDirs, file.Name())
		}
	}
	return
}

func listMusic(musicDir, genre, artist, album string) (musicList []string, err error) {
	var currentDir *os.File
	if strings.Compare(album, "") == 0 {
		currentDir, err = os.Open(musicDir + "/" + genre + "/" + artist)
		if err != nil {
			return musicList, err
		}
	} else {
		currentDir, err = os.Open(musicDir + "/" + genre + "/" + artist + "/" + album)
		if err != nil {
			return musicList, err
		}
	}
	files, err := currentDir.Readdir(-1)
	if err != nil {
		return musicList, err
	}
	for _, file := range files {
		if len(file.Name()) > 3 {
			if strings.Compare(file.Name()[len(file.Name()) - 4:], ".mp3") == 0 {
				// fmt.Println(file.Name())
				musicList = append(musicList, file.Name())
			}
		}
	}
	return
}

func editMusic (musicDir, genre, artist, album string, songs []string) (err error) {
	var file *taglib.File
	for _, absSong := range(songs) {
		// fmt.Println("song:\t-\t-\t" + absSong)
		if album == "" {
			file, err = taglib.Read(musicDir + "/" + genre + "/" + artist + "/" + absSong)
		} else {
			file, err = taglib.Read(musicDir + "/" + genre + "/" + artist + "/" + album + "/" + absSong)
		}
		if err != nil {
			return err
		}
		if file.Genre() != genre {
			file.SetGenre(genre)
		}
		if file.Artist() != artist {
			file.SetArtist(artist)
		}
		if album == "" {
			file.SetAlbum("[non-album track]")
		} else {
			file.SetAlbum(album)
		}
		if file.Title() != absSong[:len(absSong) - 4] {
			file.SetTitle(absSong[:len(absSong) - 4])
		}
		err = file.Save()
		if err != nil {
			return err
		}
		file.Close()
	}
	return err
}
