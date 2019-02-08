package com.zspirytus.enjoymusic.entity;

public class MusicMetaDataListItem {

    private boolean isArtistArt = false;

    private boolean isPreview = false;
    private Music preview;

    private boolean isTitle;
    private String title;

    private boolean isDownloadAlbumArtView = false;

    private boolean isSingleEditText = false;
    private String editTextTitle;
    private String editTextDefaultText;

    private boolean isDuplicateEditText = false;
    private String firstEditTextTitle;
    private String firstEditTextDefaultText;
    private String secondEditTextTitle;
    private String secondEditTextDefaultText;

    public boolean isArtistArt() {
        return isArtistArt;
    }

    public void setArtistArt(boolean artistArt) {
        isArtistArt = artistArt;
    }

    public boolean isPreview() {
        return isPreview;
    }

    public void setPreview(boolean preview) {
        isPreview = preview;
    }

    public Music getPreview() {
        return preview;
    }

    public void setPreview(Music preview) {
        this.preview = preview;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDownloadAlbumArtView() {
        return isDownloadAlbumArtView;
    }

    public void setDownloadAlbumArtView(boolean downloadAlbumArtView) {
        isDownloadAlbumArtView = downloadAlbumArtView;
    }

    public boolean isSingleEditText() {
        return isSingleEditText;
    }

    public void setSingleEditText(boolean singleEditText) {
        isSingleEditText = singleEditText;
    }

    public String getEditTextTitle() {
        return editTextTitle;
    }

    public void setEditTextTitle(String editTextTitle) {
        this.editTextTitle = editTextTitle;
    }

    public boolean isDuplicateEditText() {
        return isDuplicateEditText;
    }

    public void setDuplicateEditText(boolean duplicateEditText) {
        isDuplicateEditText = duplicateEditText;
    }

    public String getFirstEditTextTitle() {
        return firstEditTextTitle;
    }

    public void setFirstEditTextTitle(String firstEditTextTitle) {
        this.firstEditTextTitle = firstEditTextTitle;
    }

    public String getSecondEditTextTitle() {
        return secondEditTextTitle;
    }

    public void setSecondEditTextTitle(String secondEditTextTitle) {
        this.secondEditTextTitle = secondEditTextTitle;
    }

    public String getEditTextDefaultText() {
        return editTextDefaultText;
    }

    public void setEditTextDefaultText(String editTextDefaultText) {
        this.editTextDefaultText = editTextDefaultText;
    }

    public String getFirstEditTextDefaultText() {
        return firstEditTextDefaultText;
    }

    public void setFirstEditTextDefaultText(String firstEditTextDefaultText) {
        this.firstEditTextDefaultText = firstEditTextDefaultText;
    }

    public String getSecondEditTextDefaultText() {
        return secondEditTextDefaultText;
    }

    public void setSecondEditTextDefaultText(String secondEditTextDefaultText) {
        this.secondEditTextDefaultText = secondEditTextDefaultText;
    }
}
