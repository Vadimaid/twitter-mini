package twitter.factory;

import twitter.exception.UnknownCommandException;
import twitter.factory.command.CommandHandler;

public interface CommandFactory {


    CommandHandler getHandler(String command) throws UnknownCommandException;

}
