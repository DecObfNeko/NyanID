package moe.takanashihoshino.nyaniduserserver.utils.Command;

public  interface Command {
    String getName();

    String getDescription();
    void execute(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException;
}
