package com.tosi.tale.taleDetail;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Page {
    private int leftNo;
    private String left;
    private int rightNo;
    private String right;
    private boolean flipped;

}