package com.dantsu.escposprinter.textparser;

import com.dantsu.escposprinter.EscPosPrinterCommands;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;

import java.util.Hashtable;

public class PrinterTextParserQRCode implements IPrinterTextParserElement {

    private String data;
    private byte[] align;
    private int size;

    public PrinterTextParserQRCode(PrinterTextParserColumn printerTextParserColumn, String textAlign,
                                   Hashtable<String, String> qrCodeAttributes, String data) throws EscPosParserException {

        this.data = data;

        this.align = EscPosPrinterCommands.TEXT_ALIGN_LEFT;
        switch (textAlign) {
            case PrinterTextParser.TAGS_ALIGN_CENTER:
                this.align = EscPosPrinterCommands.TEXT_ALIGN_CENTER;
                break;
            case PrinterTextParser.TAGS_ALIGN_RIGHT:
                this.align = EscPosPrinterCommands.TEXT_ALIGN_RIGHT;
                break;
        }
        // Default size for NCR 7199
        size = 4;

        if (qrCodeAttributes.containsKey(PrinterTextParser.ATTR_QRCODE_SIZE)) {
            String qrCodeAttribute = qrCodeAttributes.get(PrinterTextParser.ATTR_QRCODE_SIZE);

            if (qrCodeAttribute == null) {
                throw new EscPosParserException("Invalid QR code attribute : " + PrinterTextParser.ATTR_QRCODE_SIZE);
            }
            try {
                size = Integer.parseInt(qrCodeAttribute);
            } catch (NumberFormatException nfe) {
                throw new EscPosParserException("Invalid QR code " + PrinterTextParser.ATTR_QRCODE_SIZE + " value");
            }
        }

    }

    @Override
    public int length() throws EscPosEncodingException {
        return 1;
    }

    @Override
    public IPrinterTextParserElement print(EscPosPrinterCommands printerSocket) throws EscPosEncodingException {

        printerSocket.setAlign(align);
        printerSocket.printQRCode(EscPosPrinterCommands.QRCODE_2, data, size);
        return this;
    }
}
