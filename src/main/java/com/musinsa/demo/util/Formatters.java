package com.musinsa.demo.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Formatters {
    public static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,###");
}