package com.tsystems.javaschool.tasks.pyramid;

import java.util.Comparator;

import java.util.List;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        // TODO : Implement your solution here

        if (inputNumbers.stream().filter(o -> o == null).count() != 0) {
            throw new CannotBuildPyramidException();
        }

        //S=(a1+an)*n/2
        //S=(n+n^2)/2
        //n^2+n-2S = 0
        //D=1+8S
        //n1,2=-1+sqrt(D)/2
        int pyramidLevel = (-1 + (int) Math.sqrt(1 + 8 * inputNumbers.size())) / 2;
        if ((pyramidLevel + pyramidLevel * pyramidLevel) / 2 != inputNumbers.size()) {
            throw new CannotBuildPyramidException();
        }

        inputNumbers.sort(Comparator.comparingInt(o -> o));

        int[][] pyramidArray = new int[pyramidLevel][1 + (pyramidLevel - 1) * 2];

        for (int i = 0; i < pyramidLevel; i++) {
            int currentLevel = i + 1;
            int endIndex = (currentLevel + currentLevel * currentLevel) / 2;
            int strartIndex = endIndex - currentLevel;

            List<Integer> levelElements = inputNumbers.subList(strartIndex, endIndex);

            int zeroPrefix = pyramidLevel - currentLevel;

            for (int j = 0; j < currentLevel; j++) {
                pyramidArray[i][zeroPrefix + j * 2] = levelElements.get(j);
            }
        }

        return pyramidArray;
    }


}

