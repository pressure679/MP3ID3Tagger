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

public class sample {
	public static void main(String[] args) {
		String musicDir = System.getenv("HOME");
		musicDir = musicDir + "\\Music";
		String[] artistsBuffer;
		String[] albumsBuffer;
		String[] musicBuffer;

		try {
			artistsBuffer = getDir(new File(musicDir));
			for (String artistsIterator : artistsBuffer) {
				musicBuffer = getMusic(new File(musicDir + "\\" + artistsIterator));
				if (musicBuffer != null) {
					for (String musicIterator : musicBuffer) {
					editMusic(musicDir, artistsIterator, "", musicIterator);
					}
					albumsBuffer = getDir(new File(musicDir + "\\" + artistsIterator));
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

	public static String[] getDir(File dir) {
		List<File> albumList = new ArrayList<File>();
		String[] buffer;
		if (dir.listFiles() != null) {
			for (File albumIterator : dir.listFiles()) {
				if (!albumIterator.isFile()) {
					albumList.add(albumIterator);
				}
			}
		}
		buffer = new String[albumList.size()];
		albumList.toArray(buffer);
		return buffer;
	}

	public static String[] getMusic(File dir) {
		List<String> musicList = new ArrayList<String>();
		String[] buffer;
		if (dir.listFiles() != null) {
			for (File musicIterator : dir.listFiles()) {
				if (dir.isFile()) {
					musicList.add(dir.getName());
				}
			}
		}
		buffer = new String[musicList.size()];
		musicList.toArray(buffer);
		return buffer;
	}

	public static void editMusic(String dir, String artist, String album, String file) throws IOException, UnsupportedTagException, NotSupportedException, InvalidDataException {
		Mp3File mp3File;
		if (album == "") {
			mp3File = new Mp3File(dir + "\\" + artist + "\\" + file + ".mp3");
		} else {
			mp3File = new Mp3File(dir + "\\" + artist + "\\" + album + "\\" + file + ".mp3");
		}

		if (mp3File.hasId3v1Tag()) {
			mp3File.removeId3v1Tag();
		}
		if (mp3File.hasCustomTag()) {
			mp3File.removeCustomTag();
		}

		ID3v2 id3v2Tag;
		if (mp3File.hasId3v2Tag()) {
			id3v2Tag = mp3File.getId3v2Tag();
		} else {
			id3v2Tag = new ID3v24Tag();
			mp3File.setId3v2Tag(id3v2Tag);
		}
		id3v2Tag.setArtist(artist);
		id3v2Tag.setTitle(file);
		id3v2Tag.setAlbum(album);

		mp3File.save(dir + "\\" + artist + "\\" + "album" + "\\" + file + ".mp3");
	}
}
