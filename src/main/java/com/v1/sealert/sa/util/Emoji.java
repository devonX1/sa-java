package com.v1.sealert.sa.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

public class Emoji {
    private static final Emoji emojiInstance = new Emoji();
    private final Map<String, String> emojiMap = new HashMap<>();

    public Map<String, String> getEmojiMap() {
        return this.emojiMap;
    }
    public static Emoji getInstance() {
        return emojiInstance;
    }

    public Emoji() {
        emojiMap.put("smiling_face", "\uD83D\uDE00"); // 😀
        emojiMap.put("winking_face", "\uD83D\uDE09"); // 😉
        emojiMap.put("heart_eyes", "\uD83D\uDE0D"); // 😍
        emojiMap.put("laughing", "\uD83D\uDE02"); // 😂
        emojiMap.put("thumbs_up", "\uD83D\uDC4D"); // 👍
        emojiMap.put("clapping", "\uD83D\uDC4F"); // 👏
        emojiMap.put("sunglasses", "\uD83D\uDE0E"); // 😎
        emojiMap.put("crying", "\uD83D\uDE22"); // 😢
        emojiMap.put("angry", "\uD83D\uDE21"); // 😡
        emojiMap.put("sleeping", "\uD83D\uDE34"); // 😴
        emojiMap.put("grinning", "\uD83D\uDE01"); // 😁
        emojiMap.put("wink", "\uD83D\uDE09"); // 😉
        emojiMap.put("blush", "\uD83D\uDE0A"); // 😊
        emojiMap.put("smirk", "\uD83D\uDE0F"); // 😏
        emojiMap.put("neutral_face", "\uD83D\uDE10"); // 😐
        emojiMap.put("expressionless", "\uD83D\uDE11"); // 😑
        emojiMap.put("confused", "\uD83D\uDE15"); // 😕
        emojiMap.put("unamused", "\uD83D\uDE12"); // 😒
        emojiMap.put("sweat_smile", "\uD83D\uDE05"); // 😅
        emojiMap.put("relieved", "\uD83D\uDE0C"); // 😌
        emojiMap.put("pensive", "\uD83D\uDE14"); // 😔
        emojiMap.put("worried", "\uD83D\uDE1F"); // 😟
        emojiMap.put("angry_face", "\uD83D\uDE20"); // 😠
        emojiMap.put("rage", "\uD83D\uDE21"); // 😡
        emojiMap.put("triumph", "\uD83D\uDE24"); // 😤
        emojiMap.put("sleepy", "\uD83D\uDE2A"); // 😪
        emojiMap.put("tired_face", "\uD83D\uDE2B"); // 😫
        emojiMap.put("scream", "\uD83D\uDE31"); // 😱
        emojiMap.put("astonished", "\uD83D\uDE32"); // 😲
        emojiMap.put("flushed", "\uD83D\uDE33"); // 😳
        emojiMap.put("hushed", "\uD83D\uDE2F"); // 😯
        emojiMap.put("frowning", "\uD83D\uDE41"); // 😁
        emojiMap.put("anguished", "\uD83D\uDE27"); // 😧
        emojiMap.put("disappointed", "\uD83D\uDE1E"); // 😞
        emojiMap.put("pensive_face", "\uD83D\uDE14"); // 😔
        emojiMap.put("relieved_face", "\uD83D\uDE0C"); // 😌
        emojiMap.put("thinking_face", "\uD83E\uDD14"); // 🤔
        emojiMap.put("zipper_mouth_face", "\uD83E\uDD10"); // 🤐
        emojiMap.put("money_mouth_face", "\uD83E\uDD11"); // 🤑
        emojiMap.put("nerd_face", "\uD83E\uDD13"); // 🤓
        emojiMap.put("sunglasses_face", "\uD83D\uDE0E"); // 😎
        emojiMap.put("party_face", "\uD83E\uDD73"); // 🥳
        emojiMap.put("hugs", "\uD83E\uDD17"); // 🤗
        emojiMap.put("kissing_heart", "\uD83D\uDE18"); // 😘
        emojiMap.put("kiss", "\uD83D\uDE19"); // 😙
        emojiMap.put("love_you", "\uD83D\uDE0D"); // 😍
        emojiMap.put("red_pin", "\uD83D\uDCCD");
    }
}