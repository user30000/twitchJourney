package chatBot;

import de.comhix.twitch.api.irc.ChatConnector;
import game.GameEventListener;

public class TwitchChat extends ChatConnector implements outMessageListener {

    private GameEventListener gameEventListener;

    public TwitchChat(String name) {
        super(name);
    }

    /**
     *
     * @param channel Канал, с которого пришло сообщение
     * @param sender - хз
     * @param login Логин отправителя
     * @param hostname - хз
     * @param message Сообщение отправителя
     */

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        super.onMessage(channel, sender, login, hostname, message);
        this.log(channel + " " + login + ":" + message);

        //TODO Сделать обработчик сообщений
        if("!red".toLowerCase().equals(message)){
            gameEventListener.GameEvent(this, "red");
        }
        if("!green".toLowerCase().equals(message)){
            gameEventListener.GameEvent(this, "green");
        }
        if("!blue".toLowerCase().equals(message)){
            gameEventListener.GameEvent(this, "blue");
        }

        /*if("!rulet".equals(message)){
            if(Game.Players.get(login) == null){
                this.sendMessage("#user30000", login + " Собака ты не мытая, хочешь покончить с собой раньше чем зарегистрируешься в игре? Команда: !init");
            }
            if(Game.Players.get(login) != null)
            {
                if(new Random().nextInt(6) == 4) {
                    this.sendMessage("#user30000", login + " Вышиб себе мозги");
                    Game.Players.remove(login);
                }
                else{
                    this.sendMessage("#user30000", login + " Выстрелил себе в голову, но промазал");
                }
            }
        }

        if("!plus".equals(message)){
            JoglCanvas.changePxlSize(true);
        }
        if("!minus".equals(message)){
            JoglCanvas.changePxlSize(false);
        }

        if("!init".equals(message)){
            Player p = new Player(100, login);
            if(Game.Players.get(login) == null) {
                Game.Players.put(login, p);
            }
        }

        if(message.startsWith("!kick")){
            String who = message.substring(5).toLowerCase().trim();
            System.out.println(who);
            if(Game.Players.get(who) != null)
            {
                Game.Players.get(login).getDamage(new Random().nextInt(5) + 20);
                //this.sendMessage("#user30000", ;
            }
        }

        if("!cry".equals(message)){
            if(Game.Players.get(login) != null)
            {
                this.sendMessage("#user30000", Game.Players.get(login).Yell());
            }
        }*/
    }

    public void setGameEventListener(GameEventListener listener){
        this.gameEventListener = listener;
    }

    @Override
    public void Write(String Message) {
        //this.sendMessage("#user30000", Message);
        //System.out.println(Message);
    }
}