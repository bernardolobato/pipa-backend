package br.com.bernardolobato.pipa.service;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

import br.com.bernardolobato.pipa.controller.dto.ScoreFormDTO;
import br.com.bernardolobato.pipa.controller.dto.ScoreResultDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class ScoreService {
    private Map<Long, Long> scores = new ConcurrentHashMap<>();
    List<ScoreFormDTO> sortedIds = new ArrayList<>();
    private boolean dirty;
    private List<ScoreResultDTO> highscores = new ArrayList<>();
    private final Integer MAX_HIGHSCORE = 20_000;
    public void addScore(ScoreFormDTO score) {
        this.scores.compute(score.getUserId(), (k, v) -> v == null ? score.getPoints() : v + score.getPoints());
        //this.init();
        this.dirty = true;
    }

    private void sort() {
        if (this.dirty) {
            this.sortedIds = scores.entrySet().stream()
            .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
            //.map(Map.Entry::getKey)
            .map((el)-> new ScoreFormDTO(el.getKey(), el.getValue()))
            .collect(Collectors.toList());
        }
    }

    public ScoreResultDTO getPosition(Long userId) {
        this.sort();
        int position = Collections.binarySearch(this.sortedIds, userId, (form, uid)-> {
                return ((ScoreFormDTO)form).getUserId().compareTo((Long)uid) ;
            }
        );
        //int position = this.sortedIds.indexOf(userId);
        //Integer score = this.scores.get(userId);
        if (position < 0) {
            return null;
        }
        ScoreFormDTO score = this.sortedIds.get(position);
        this.dirty = false;
        return new ScoreResultDTO(score.getUserId(), score.getPoints(), position);
    }

    public List<ScoreResultDTO> getHighScoreList() {
        this.sort();
        if (dirty) {
            ListIterator<ScoreFormDTO> i = null;
            if (this.sortedIds.size() > 20_000) {
                i = this.sortedIds.subList(0, MAX_HIGHSCORE).listIterator();
            } else {
                i = this.sortedIds.listIterator();
            }
            highscores = new ArrayList<>();
            while(i.hasNext()) {
                int index = i.nextIndex();
                ScoreFormDTO value = i.next();
                highscores.add(new ScoreResultDTO(value.getUserId(), value.getPoints(), index));
            }   
        }
        this.dirty = false;
        return highscores;
    }

    // private void init() {
    //     int i = 0;
    //     while(i<=20_000) {
    //         this.scores.put(new Random().nextInt(20_000), new Random().nextInt(20_000));
    //         i++;
    //     }        
    // }
}