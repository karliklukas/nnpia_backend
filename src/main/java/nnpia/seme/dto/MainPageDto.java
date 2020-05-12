package nnpia.seme.dto;

public class MainPageDto {
    private Long countFree;
    private Long countDone;
    private Long countWait;

    public Long getCountFree() {
        return countFree;
    }

    public void setCountFree(Long countFree) {
        this.countFree = countFree;
    }

    public Long getCountDone() {
        return countDone;
    }

    public void setCountDone(Long countDone) {
        this.countDone = countDone;
    }

    public Long getCountWait() {
        return countWait;
    }

    public void setCountWait(Long countWait) {
        this.countWait = countWait;
    }
}
