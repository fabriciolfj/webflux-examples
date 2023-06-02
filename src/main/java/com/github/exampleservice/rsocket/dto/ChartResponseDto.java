package com.github.exampleservice.rsocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChartResponseDto {

    private int input;
    private int output;

    @Override
    public String toString() {
        final String graphFormat = getFormat(this.output);
        return String.format(graphFormat, this.input, "X");
    }

    private String getFormat(int value) {
        return "%3s|%" + value + "s";
    }
}
