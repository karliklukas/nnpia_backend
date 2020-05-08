package nnpia.seme.model;

public class StatusDto {
    private boolean done;
    private boolean haveUser;

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isHaveUser() {
        return haveUser;
    }

    public void setHaveUser(boolean haveUser) {
        this.haveUser = haveUser;
    }
}
