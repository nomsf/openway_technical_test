package org.example;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private static final Dotenv dotenv = Dotenv.load();

    // Environment Variables
    public static final String EMAIL = dotenv.get("EMAIL");
    public static final String PASSWORD = dotenv.get("PASSWORD");
    public static final String URL = dotenv.get("URL");
    public static final String PRODUCT_URL = dotenv.get("PRODUCT_URL");
    public static final String CART_URL = dotenv.get("CART_URL");
    public static final String ACCOUNT_URL = dotenv.get("ACCOUNT_URL");
    public static final String LOGIN_REDIRECT_URL = dotenv.get("LOGIN_REDIRECT_URL");
    public static final String LOGIN_URL = dotenv.get("LOGIN_URL");
}

