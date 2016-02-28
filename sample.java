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
import java.util.Iterator;

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
				
				// edit mp3 files from artists
				if (musicBuffer != null) {
					for (String musicIterator : musicBuffer) {
						editMusic(musicDir, artistsIterator, "", musicIterator);
					}

					// gets albums from artists
					albumsBuffer = getDir(new File(artistsIterator));

					// edits music from an album from an arist
					if (albumsBuffer != null) {
						for (String albumsIterator : albumsBuffer) {
							musicBuffer = getMusic(new File(albumsIterator));
							if (musicBuffer != null) {
								for (String musicIterator : musicBuffer) {
									editMusic(musicDir, artistsIterator, albumsIterator, musicIterator);
								}
							}
						}
					}
				}
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
				musicList.add(music);
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
		ID3v1 id3v1Tag;
		ID3v2 id3v2Tag;
		if (!file.substring(file.length() - 3, file.length()).equals("mp3")) {
				return;
		}
		artist = artist.split("/")[4];
		album = album.split("/")[5];
		if (album.equals("")) {
			mp3File = new Mp3File(dir + "/" + artist + "/" + file);
		} else {
			mp3File = new Mp3File(dir + "/" + artist + "/" + album + "/" + file);
		}
		
		if (mp3File.hasId3v1Tag()) {
			System.out.println("id3v1 tag");
			id3v1Tag = mp3File.getId3v1Tag();
		} else if (mp3File.hasId3v2Tag()) {
			System.out.println("id3v2 tag");
			id3v2Tag = mp3File.getId3v2Tag();
			id3v2Tag.setArtist(artist);
			id3v2Tag.setAlbum(album);
			if ((file.substring(file.length() - 3, file.length())).equals("mp3")) {
				id3v2Tag.setTitle(file.substring(0, file.length() - 4));
			} else {
				id3v2Tag.setTitle(file);
			}
			mp3File.setId3v2Tag(id3v2Tag);
		} else {
			id3v1Tag = new ID3v1Tag();
			id3v1Tag.setArtist(artist);
			id3v1Tag.setAlbum(album);
			if ((file.substring(file.length() - 3, file.length())).equals("mp3")) {
				id3v1Tag.setTitle(file.substring(0, file.length() - 4));
			} else {
				id3v1Tag.setTitle(file);
			}
			mp3File.setId3v1Tag(id3v1Tag);
		}	
		
		if (album.equals("")) {
			mp3File.save(dir + "/" + artist + "/" + file);
		} else {
			mp3File.save(dir + "/" + artist + "/" + album + "/" + file);
		}
	}
}
