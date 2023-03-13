/*
 * 
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * 
 */

// Write your code here
package com.example.song.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import com.example.song.model.Song;
import com.example.song.model.SongRowMapper;
import com.example.song.repository.SongRepository;
 



@Service
public class SongH2Service implements SongRepository{

    @Autowired
    JdbcTemplate db;

	@Override
	public ArrayList<Song> getSongs() {
		List<Song> songsList =  db.query("SELECT * FROM playlist",new SongRowMapper());
		ArrayList<Song> songs = new ArrayList<>(songsList);
		return songs;
		
	}

	@Override
	public Song getSongById(int songId) {
		try{
			Song song = db.queryForObject("SELECT * FROM playlist where songId  = ?",new SongRowMapper(),songId);
			return song;
		}catch(Exception e){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		
	}

	@Override
	public Song addSong(Song song) {
		String addSongQuery = "INSERT INTO playlist(songName,lyricist,singer,musicDirector) VALUES(?,?,?,?)";
		db.update(addSongQuery,song.getSongName(),song.getLyricist(),song.getSinger(),song.getMusicDirector());
		Song savedSong = db.queryForObject("SELECT * FROM playlist where songName  = ?",new SongRowMapper(),song.getSongName());
		return savedSong;

	}

	@Override
	public Song updateSong(int songId,Song song) {
		Song existingSong = getSongById(songId);
		if (existingSong == null){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		if (song.getSongName() != null){
			db.update("UPDATE playlist SET songName = ? where songId = ?", song.getSongName(),songId);
		}
		if (song.getLyricist() != null){
			db.update("UPDATE playlist SET lyricist = ? where songId = ?", song.getLyricist(),songId);
		}

		if (song.getSinger() != null){
			db.update("UPDATE playlist SET singer = ? where songId = ?", song.getSinger(),songId);
		}
		if (song.getMusicDirector() != null){
			db.update("UPDATE playlist SET musicDirector = ? where songId = ?", song.getMusicDirector(),songId);
		}


		return getSongById(songId);




	}

	@Override
	public void deleteSong(int songId) {			
		db.update("DELETE FROM playlist WHERE songid = ?",songId);


	}

}
