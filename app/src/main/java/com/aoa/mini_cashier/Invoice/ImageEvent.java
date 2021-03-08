package com.aoa.mini_cashier.Invoice;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;

class ImageEvent implements PdfPCellEvent {
    protected Image img;
    public ImageEvent(Image img) {
        this.img = img;
    }
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        img.scaleToFit(position.getWidth(), position.getHeight());
        img.setAbsolutePosition(position.getLeft() + (position.getWidth() - img.getScaledWidth()) / 2,
                position.getBottom() + (position.getHeight() - img.getScaledHeight()) / 2);
        PdfContentByte canvas = canvases[PdfPTable.BACKGROUNDCANVAS];
        try {
            canvas.addImage(img);
        } catch (DocumentException ex) {
            // do nothing
        }
    }
}



