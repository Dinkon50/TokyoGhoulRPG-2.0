package com.tokyoghoul.rpg.v2;

public enum KaguneType {
    NONE("Нет", "Человек / CCG"),
    RINKAKU("Rinkaku", "Ближний бой, регенерация, рывки"),
    UKAKU("Ukaku", "Скорость и дальние атаки"),
    KOUKAKU("Koukaku", "Защита и тяжёлые удары"),
    BIKAKU("Bikaku", "Сбалансированный стиль"),
    KAGERO("Kagerō", "Уникальный гибридный тип полугулю");

    private final String displayName;
    private final String description;
    KaguneType(String displayName, String description) { this.displayName = displayName; this.description = description; }
    public String displayName() { return displayName; }
    public String description() { return description; }
}
