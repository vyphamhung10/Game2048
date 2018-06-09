package com.gdx.game2048.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gdx.game2048.MainGame;
import com.gdx.game2048.manager.ScreenManager;
import javafx.util.Pair;

import java.awt.*;
import java.util.LinkedHashMap;

public class MenuScreen extends AbstractScreen {
    //Game logic

    //Tag for debug
    private static final String TAG = MenuView.class.getSimpleName();

    //Animation constant
    public static final int BASE_ANIMATION_TIME = 100;
    private static final float MERGING_ACCEL = -0.5f;
    private static final float INITIAL_VELO = (1 - MERGING_ACCEL) / 4;

    //Cell render
    public final int numCellTypes = 21;
    public Rectangle gridRect = new Rectangle();
    //Queue to draw cell text
    private int cellSize;
    private int cellPadding;

    //Step 1: create Skin, TextureAtlas, Button
    //Button

    public Skin gameSkin;

    public TextureAtlas gameAtlas;
    public TextureAtlas nextLevelButtonAtlas;
    public TextureAtlas preLevelButtonAtlas;
    public TextureAtlas nextAIButtonAtlas;
    public TextureAtlas preAIButtonAtlas;
    public TextureAtlas imageLevelAtlas;

    public com.badlogic.gdx.scenes.scene2d.ui.Button nextLevelButton;
    public com.badlogic.gdx.scenes.scene2d.ui.Button preLevelButton;
    public com.badlogic.gdx.scenes.scene2d.ui.Button nextAIButton;
    public com.badlogic.gdx.scenes.scene2d.ui.Button preAIButton;
    public Image imageLevel;

    public int iconSize;
    public int iconPaddingSize;

    //Text
    public FreeTypeFontGenerator fontGenerator;
    public FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    public BitmapFont scoreFont;


    public TextButton levelText;
    public TextButton startGameText;
    public TextButton aiText;
    public TextButton startAIText;

    LinkedHashMap<Integer, Pair<String,String>> levelInfo;
    int curLevel = 3;

    LinkedHashMap<Integer, String> aiInfo;
    int curAI = 1;



    //Timing for draw
    private long lastFPSTime = System.currentTimeMillis();
    public boolean refreshLastTime = false;

    //Music
    public Music mainTheme;

    //Batch for drawing;
    private SpriteBatch batch;

    @Override
    public void buildStage() {
        batch = new SpriteBatch();

        levelInfo = new LinkedHashMap<Integer, Pair<String, String>>();
        levelInfo.put(3, new Pair<String, String>("3x3", "3x3"));
        levelInfo.put(4, new Pair<String, String>("4x4", "4x4"));
        levelInfo.put(5, new Pair<String, String>("5x5", "5x5"));
        levelInfo.put(6, new Pair<String, String>("6x6", "6x6"));

        aiInfo = new LinkedHashMap<Integer, String>();
        aiInfo.put(1, "Dump AI");
        aiInfo.put(2, "Smart AI");
        aiInfo.put(3, "Crazy AI");

        //Loading asset
        mainTheme = Gdx.audio.newMusic(Gdx.files.internal("music/maintheme.mp3"));
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ClearSans-Bold.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        //Step 2: TextureAtlas with atlas path
        gameAtlas = new TextureAtlas("themes/menu.atlas");

        //Step 3: create skin
        gameSkin = new Skin(gameAtlas);

        //setup button
        createButton();

        //Step 6: add to stage
        this.addActor(nextLevelButton);
        this.addActor(preLevelButton);
        this.addActor(nextAIButton);
        this.addActor(preAIButton);
        this.addActor(imageLevel);

        this.addActor(levelText);
        this.addActor(startGameText);
        this.addActor(aiText);
        this.addActor(startAIText);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        handleInput();
        //Reset the transparency of the screen
//        Gdx.gl.glClearColor(1, 1, 1, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        //Draw cell shape
        batch.end();


        //For now render cells only
    }

    @Override
    public void resize(int width, int height) {
        makeLayout(width, height);
        super.resize(width, height);
    }

    private void makeLayout(int width, int height) {

        int screenMidX = width / 2;
        int screenMidY = height / 2;
        iconSize = 50;
        iconPaddingSize = 50;
        float imageSize = 200;

        // Step 7 : set position
        int buttonPaddingMidX = width/4;

        nextLevelButton.setPosition(screenMidX + buttonPaddingMidX + iconSize/2 , height* 0.625f, Align.center);
        nextLevelButton.setSize(iconSize, iconSize);

        preLevelButton.setPosition(screenMidX - buttonPaddingMidX + iconSize/2 , height* 0.625f, Align.center);
        preLevelButton.setSize(iconSize, iconSize);

        nextAIButton.setPosition(screenMidX + buttonPaddingMidX*1.3f + iconSize/2  , height* 0.35f, Align.center);
        nextAIButton.setSize(iconSize, iconSize);

        preAIButton.setPosition(screenMidX - buttonPaddingMidX*1.3f + iconSize/2  , height* 0.35f, Align.center);
        preAIButton.setSize(iconSize, iconSize);

        imageLevel.setPosition(width*1.03f - imageSize/2 , height* 0.97f, Align.center);
        imageLevel.setSize(imageSize, imageSize);


        int textSize = fontParameter.size;
        levelText.setPosition(screenMidX, height* 0.59f, Align.center);
        startGameText.setPosition(screenMidX, height* 0.50f - textSize/2, Align.center);
        aiText.setPosition(screenMidX, height* 0.345f  - textSize/2, Align.center);
        startAIText.setPosition(screenMidX, height* 0.23f  - textSize/2, Align.center);

    }

    public void resyncTime() {
        lastFPSTime = System.currentTimeMillis();
    }

    private void nextLevel() {
        if (curLevel != 6) {
            curLevel++;
        } else {
            curLevel = 3;
        }

        levelText.setText(levelInfo.get(curLevel).getKey());
        imageLevel.setDrawable(gameSkin.getDrawable(levelInfo.get(curLevel).getValue()));
    }

    private void preLevel() {
        if (curLevel != 3) {
            curLevel--;
        } else {
            curLevel = 6;
        }

        levelText.setText(levelInfo.get(curLevel).getKey());
        imageLevel.setDrawable(gameSkin.getDrawable(levelInfo.get(curLevel).getValue()));
    }

    private void nextAI() {
        if (curAI != 3) {
            curAI++;
        } else {
            curAI = 1;
        }

        aiText.setText(aiInfo.get(curAI));
    }

    private void preAI() {
        if (curAI != 1) {
            curAI--;
        } else {
            curAI = 3;
        }

        aiText.setText(aiInfo.get(curAI));
    }

    private void createButton() {

        //Step 4: create button
        nextLevelButton = new com.badlogic.gdx.scenes.scene2d.ui.Button(gameSkin.getDrawable("ic_next"), gameSkin.getDrawable("ic_next"));
        preLevelButton = new com.badlogic.gdx.scenes.scene2d.ui.Button(gameSkin.getDrawable("ic_pre"), gameSkin.getDrawable("ic_pre"));
        nextAIButton = new com.badlogic.gdx.scenes.scene2d.ui.Button(gameSkin.getDrawable("ic_next"), gameSkin.getDrawable("ic_next"));
        preAIButton = new Button(gameSkin.getDrawable("ic_pre"), gameSkin.getDrawable("ic_pre"));
        imageLevel = new Image(gameSkin.getDrawable(levelInfo.get(curLevel).getValue()));

        //Step 5: add event
        nextLevelButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                nextLevel();
            }
        });
        preLevelButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                preLevel();
            }
        });
        nextAIButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                nextAI();
            }
        });
        preAIButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                preAI();
            }
        });

        fontParameter.size = Gdx.graphics.getWidth() / 11;
        fontParameter.color = Color.valueOf("#efb75d");
        scoreFont = fontGenerator.generateFont(fontParameter);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = scoreFont;

        levelText = new TextButton(levelInfo.get(curLevel).getKey(), textButtonStyle);
        startGameText = new TextButton("Start Game", textButtonStyle);
        aiText = new TextButton(aiInfo.get(curAI), textButtonStyle);
        startAIText = new TextButton("Play With Help" , textButtonStyle);

        startGameText.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // dùng biến curLevel để xét
                ScreenManager.getInstance().showScreen(ScreenEnum.GAME, curLevel, curLevel);
            }
        });

        startAIText.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // dùng biến curAI để xét
            }
        });
    }

    private void handleInput(){
        //Don't handle input while there is an active animation

        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            System.out.println("Move up");
            nextAI();

        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            System.out.println("Move ");
            preAI();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            System.out.println("Move left");
            nextLevel();

        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
            System.out.println("Move right");
            preLevel();
        }
    }

}