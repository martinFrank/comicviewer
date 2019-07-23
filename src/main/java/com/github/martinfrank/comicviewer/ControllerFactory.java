package com.github.martinfrank.comicviewer;

import javafx.util.Callback;

public class ControllerFactory implements Callback<Class<?>, Object> {

    private final StructureLoader comicLoader;

    public ControllerFactory(StructureLoader comicLoader) {
        this.comicLoader = comicLoader;
    }

    @Override
    public Object call(Class<?> type) {
        if (type == ViewerController.class) {
            return new ViewerController(comicLoader);
//        }
//        else if (type == CardStackController.class) {
//            CardStackController cardStackController = new CardStackController(game);
//            game.setCardStackController(cardStackController);
//            return cardStackController;
//        }else if (type == MenuController.class) {
//            return new MenuController(game);
//        } else if (type == CardSlotController.class) {
//            CardSlotController cardStackController = new CardSlotController(game);
//            game.addCardSlotController(cardStackController);
//            return cardStackController;
//        } else if (type == PlayerDeckController.class) {
//            PlayerDeckController playerDeckController = new PlayerDeckController(game);
//            game.setPlayerDeckController(playerDeckController);
//            return playerDeckController;
        } else {
            // default behavior for controllerFactory:
            try {
                return type.newInstance();
            } catch (Exception exc) {
                exc.printStackTrace();
                throw new RuntimeException(exc); // fatal, just bail...
            }
        }
    }

}
