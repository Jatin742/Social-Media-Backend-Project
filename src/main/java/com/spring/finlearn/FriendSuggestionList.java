package com.spring.finlearn;
import java.util.List;
public class FriendSuggestionList {
    private List<String> suggestions;
    FriendSuggestionList(List<String> suggestions){
        this.suggestions=suggestions;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }
}
