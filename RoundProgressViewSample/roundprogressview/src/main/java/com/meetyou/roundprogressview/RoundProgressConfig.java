package com.meetyou.roundprogressview;

/**
 * Created by lwh on 2015/10/15.
 */
public class RoundProgressConfig {
    //半径
    //private int Radius;
    //渐变开始颜色
    private int StartColor;
    //渐变结束颜色
    private int EndColor;
    //内容颜色
    private int TextColor;
    private int TextSize=12;
    //内容单位颜色
    private int TextUnitColor;
    private int TextUnitSize=10;
    //圆圈背景颜色
    private int CircleBgColor;
    //内圆颜色
    private int CircleInsideColor;
    //进度条宽度
    private int ProgressBarWidth;
    //最大进度
    private int MaxProgress=100;
    //最小进度
    private int MinProgress=0;
    //单位名称
    private String UnitText="%";

    public String getUnitText() {
        return UnitText;
    }

    public RoundProgressConfig setUnitText(String unitText) {
        UnitText = unitText;
        return this;
    }

    public int getTextSize() {
        return TextSize;
    }

    public RoundProgressConfig setTextSize(int textSize) {
        TextSize = textSize;
        return this;
    }

    public int getTextUnitSize() {
        return TextUnitSize;
    }

    public RoundProgressConfig setTextUnitSize(int textUnitSize) {
        TextUnitSize = textUnitSize;
        return this;
    }

   /* public int getRadius() {
        return Radius;
    }

    public RoundProgressConfig setRadius(int radius) {
        Radius = radius;
        return this;
    }*/



    public int getStartColor() {
        return StartColor;
    }

    public RoundProgressConfig setStartColor(int startColor) {
        StartColor = startColor;
        return this;
    }

    public int getEndColor() {
        return EndColor;
    }

    public RoundProgressConfig setEndColor(int endColor) {
        EndColor = endColor;
        return this;
    }

    public int getTextColor() {
        return TextColor;
    }

    public RoundProgressConfig setTextColor(int textColor) {
        TextColor = textColor;
        return this;
    }

    public int getTextUnitColor() {
        return TextUnitColor;
    }

    public RoundProgressConfig setTextUnitColor(int textUnitColor) {
        TextUnitColor = textUnitColor;
        return this;
    }

    public int getCircleBgColor() {
        return CircleBgColor;
    }

    public RoundProgressConfig setCircleBgColor(int circleBgColor) {
        CircleBgColor = circleBgColor;
        return this;
    }

    public int getCircleInsideColor() {
        return CircleInsideColor;
    }

    public RoundProgressConfig setCircleInsideColor(int circleInsideColor) {
        CircleInsideColor = circleInsideColor;
        return this;
    }


    public int getProgressBarWidth() {
        return ProgressBarWidth;
    }

    public RoundProgressConfig setProgressBarWidth(int progressBarWidth) {
        ProgressBarWidth = progressBarWidth;
        return this;
    }

    public int getMaxProgress() {
        return MaxProgress;
    }

    public RoundProgressConfig setMaxProgress(int maxProgress) {
        MaxProgress = maxProgress;
        return this;
    }

    public int getMinProgress() {
        return MinProgress;
    }

    public RoundProgressConfig setMinProgress(int minProgress) {
        MinProgress = minProgress;
        return this;
    }
}
