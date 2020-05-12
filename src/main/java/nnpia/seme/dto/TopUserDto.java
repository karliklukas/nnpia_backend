package nnpia.seme.dto;

public class TopUserDto {
    private String username;
    private Long count;

    public String getUsername() {
        return username;
    }

    public TopUserDto(String username, Long count) {
        this.username = username;
        this.count = count;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
