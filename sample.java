import java.io.File;
import java.io.IOException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.InvalidDataException;
import java.util.List;
import java.util.ArrayList;

public class sample {
	public static void main(String[] args) {
		String musicDir = System.getenv("HOME");
		musicDir = musicDir + "/Music";
		String[] artistsBuffer;
		String[] albumsBuffer;
		String[] musicBuffer;

		try {
			
			// gets artists
			artistsBuffer = getDir(new File(musicDir));
			
			// iterate artists
			for (String artistsIterator : artistsBuffer) {
				musicBuffer = getMusic(new File(musicDir + "/" + artistsIterator));
				System.out.println("artist:\t" + artistsIterator);
				// edit mp3 files from artists
				for (String musicIterator : musicBuffer) {
					editMusic(musicDir, artistsIterator, "", musicIterator);
					System.out.println("title:\t" + musicIterator.substring(0, musicIterator.length() - 4));
				}

				// gets albums from artists
				albumsBuffer = getDir(new File(musicDir + "/" + artistsIterator));
				// edits music from an album from an arist
				if (albumsBuffer != null) {
					for (String albumsIterator : albumsBuffer) {
						musicBuffer = getMusic(new File(musicDir + "/" + artistsIterator + "/" + albumsIterator));
						System.out.println("album:\t" + albumsIterator);
						if (musicBuffer != null) {
							for (String musicIterator : musicBuffer) {
								editMusic(musicDir, artistsIterator, albumsIterator, musicIterator);
								System.out.println("title:\t" + musicIterator.substring(0, musicIterator.length() - 4));
							}
						}
					}
				}
				System.out.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedTagException e) {
			e.printStackTrace();
		} catch (NotSupportedException e) {
			e.printStackTrace();
		} catch (InvalidDataException e) {
			e.printStackTrace();
		}
	}

	public static String[] getDir(File dir) { // gets directories
		List<String> albumList = new ArrayList<String>();
		String[] buffer;
		if (dir.listFiles() != null) {
			for (File albums : dir.listFiles()) {
				if (albums.isDirectory()) {
					albumList.add(albums.getName());
				}
			}
		}
		buffer = new String[albumList.size()];
		albumList.toArray(buffer);
		return buffer;
	}

	public static String[] getMusic(File dir) { // gets .mp3 files
		List<String> musicList = new ArrayList<String>();
		String[] buffer;
		if (dir.listFiles() != null) {
			for (File music : dir.listFiles()) {
				if (music.isFile()) {
					if (music.getName().substring(music.getName().length() - 3, music.getName().length()).equals("mp3")) {
						musicList.add(music.getName());
					}
				}
			}
		}
		buffer = new String[musicList.size()];
		musicList.toArray(buffer);
		return buffer;
	}

	public static void editMusic(String dir, String artist, String album, String file) throws IOException, UnsupportedTagException, NotSupportedException, InvalidDataException {
		Mp3File mp3File;
		if (!file.substring(file.length() - 3, file.length()).equals("mp3")) {
				return;
		}
		/* System.out.println("artist: " + artist);
		System.out.println("album: " + album);
		System.out.println("title: " + file); */
		if (album.equals("")) {
			mp3File = new Mp3File(dir + "/" + artist + "/" + file);
		} else {
			mp3File = new Mp3File(dir + "/" + artist + "/" + album + "/" + file);
		}
		if (file.substring(file.length() - 4, file.length()).equals(".mp3")) {
			file = file.substring(0, file.length() - 4);
		}
		
		if (mp3File.hasId3v1Tag()) {
			ID3v1 id3v1Tag = new ID3v1Tag();
			id3v1Tag.setArtist(artist);
			id3v1Tag.setAlbum(album);
			id3v1Tag.setTitle(file);
			mp3File.setId3v1Tag(id3v1Tag);
		} else if (mp3File.hasId3v2Tag()) {
			ID3v2 id3v2Tag = new ID3v24Tag();
			id3v2Tag.setArtist(artist);
			id3v2Tag.setAlbum(album);
			id3v2Tag.setTitle(file);
			mp3File.setId3v2Tag(id3v2Tag);
		}
		/* if (mp3File.hasCustomTag()) {
			System.out.println("id3v1");
		  mp3File.removeCustomTag();
			} */
		if (album.equals("")) {
			mp3File.save(dir + "/" + artist + "/" + file + ".mp3");
		} else {
			mp3File.save(dir + "/" + artist + "/" + album + "/" + file + ".mp3");
		}
	}
}
