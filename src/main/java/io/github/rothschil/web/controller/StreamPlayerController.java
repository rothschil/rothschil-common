package io.github.rothschil.web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StreamPlayerController {

    @GetMapping("/stream/player")
    public String player(Model model){
        String flvUrl ="http://127.0.0.1:9759/steam/1";
        model.addAttribute("videoUrl",flvUrl);
        return "stream-player";
    }
}
