package com.stranger.major.global;

import com.stranger.major.model.Product;

import java.util.ArrayList;
import java.util.List;

public class GlobalCart {
    public static List<Product> cart;
    static {
        cart = new ArrayList<Product>();
    }
}
