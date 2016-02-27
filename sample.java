import java.io.File;
import java.io.IOException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.InvalidDataException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class sample {
	public static void main(String[] args) {
		String musicDir = System.getenv("HOME");
		musicDir = musicDir + "/Music";
		System.out.println(musicDir);
		String[] artistsBuffer;
		String[] albumsBuffer;
		String[] musicBuffer;

		try {
			
			// gets artists
			artistsBuffer = getDir(new File(musicDir));
			
			// iterate artists
			for (String artistsIterator : artistsBuffer) {
				musicBuffer = getMusic(new File(musicDir + "/" + artistsIterator));
				System.out.println("-\tartistsIterator: " + artistsIterator);
				
				// edit mp3 files from artists
				if (musicBuffer != null) {
					for (String musicIterator : musicBuffer) {
						editMusic(musicDir, artistsIterator, "", musicIterator);
						System.out.println("-\t-\tmusicIterator: " + musicIterator);
						System.out.println("\t-\teditMusic: " + musicDir + " " + artistsIterator + " " + musicIterator);
					}

					// gets albums from artists
					albumsBuffer = getDir(new File(artistsIterator));
					System.out.println("\t-\tgetDir: " + artistsIterator);

					// edits music from an album from an arist
					if (albumsBuffer != null) {
						for (String albumsIterator : albumsBuffer) {
							musicBuffer = getMusic(new File(albumsIterator)); // doesn't get music in Fetty Wap - Deluxe
							System.out.println("\t-\t-\tgetMusic: " + albumsIterator);
							for (String s : musicBuffer) {
								System.out.println("\t-\t-\t" + s);
							}
							if (musicBuffer != null) {
								for (String musicIterator : musicBuffer) {
									System.out.println("\t-\t-\t-\tmusicIterator: " + musicIterator);
									editMusic(musicDir, artistsIterator, albumsIterator, musicIterator);
									System.out.println("\t-\t-\t-\teditMusic: " + musicDir + " " + artistsIterator + " " + musicIterator);
								}
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
		List<File> albumList = new ArrayList<File>();
		Iterator<File> albumIterator = null;
		String[] buffer;
		int counter = -1;
		if (dir.listFiles() != null) {
			for (File albums : dir.listFiles()) {
				if (!albums.isFile()) {
					albumList.add(albums);
				}
			}
			albumIterator = albumList.iterator();
		}
		buffer = new String[albumList.size()];
		if (albumIterator != null) {
			while (albumIterator.hasNext()) {
				counter++;
				buffer[counter] = albumIterator.next().toString();
			}
		}
		return buffer;
	}

	public static String[] getMusic(File dir) { // gets .mp3 files
		List<String> musicList = new ArrayList<String>();
		Iterator<String> musicIterator = null;
		String[] buffer;
		int counter = -1;
		if (dir.list() != null) {
			for (String music : dir.list()) {
				//if (dir.isFile()) {
				System.out.println("\tgetMusic() music: " + music);
				musicList.add(music);
				//}
			}
			musicIterator = musicList.iterator();
		}
		buffer = new String[musicList.size()];
		if (musicIterator != null) {
			while (musicIterator.hasNext()) {
				counter++;
				buffer[counter] = musicIterator.next();
			}
		}
		buffer = new String[musicList.size()];
		musicList.toArray(buffer);
		return buffer;
	}

	public static void editMusic(String dir, String artist, String album, String file) throws IOException, UnsupportedTagException, NotSupportedException, InvalidDataException {
		Mp3File mp3File;
		ID3v2 id3v2Tag = new ID3v24Tag();
		if (!file.substring(file.length() - 3, file.length()).equals("mp3")) {
				return;
		}
		artist = artist.split("/")[4];
		album = album.split("/")[5];
		System.out.println(dir);
		System.out.println(artist);
		System.out.println(album);
		System.out.println(file);
		if (album == "") {
			mp3File = new Mp3File(dir + "/" + artist + "/" + file);
		} else {
			mp3File = new Mp3File(dir + "/" + artist + "/" + album + "/" + file);
		}

		if (mp3File.hasId3v1Tag()) {
			mp3File.removeId3v1Tag();
		}
		if (mp3File.hasId3v2Tag()) {
			mp3File.removeId3v2Tag();
		}
		if (mp3File.hasCustomTag()) {
			mp3File.removeCustomTag();
		}

		id3v2Tag.setArtist(artist);
		id3v2Tag.setAlbum(album);
		id3v2Tag.setTitle(file);

		if (album.equals("")) {
			mp3File.save(dir + "/" + artist + "/" + file);
			System.out.println("saved " + dir + "/" + artist + "/" +  file);
		} else {
			mp3File.save(dir + "/" + artist + "/" + album + "/" + file);
			System.out.println("saved " + dir + "/" + artist + "/" + album + "/" +  file);
		}
		System.out.println("saved " + file);
	}
}
