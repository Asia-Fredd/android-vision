/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.samples.vision.ocrreader;

import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.samples.vision.ocrreader.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import asia.fredd.tools.creditcardutils.base.CardDateThru;
import asia.fredd.tools.creditcardutils.base.CardType;
import asia.fredd.tools.creditcardutils.base.CreditCard;

/**
 * A very simple Processor which gets detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 */
public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {

    private GraphicOverlay<OcrGraphic> graphicOverlay;

    OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay) {
        graphicOverlay = ocrGraphicOverlay;
    }

    /**
     * Called by the detector to deliver detection results.
     * If your application called for it, this could be a place to check for
     * equivalent detections by tracking TextBlocks that are similar in location and content from
     * previous frames, or reduce noise by eliminating TextBlocks that have not persisted through
     * multiple detections.
     */
    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        graphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        int count = items.size();
        TextBlock item;
        String text;
        CardType card = null;
        CardDateThru dateThru = null;
        for (int i = 0; i < count; ++i) {
            if ((item = items.valueAt(i)) != null && (text = item.getValue()) != null) {
                if (card == null) {
                    card = CreditCard.ExtractCardNumber(text);
                    if (card != null) {
                        OcrGraphic ocrGraphic = new OcrGraphic(graphicOverlay, item);
                        graphicOverlay.add(ocrGraphic);
                    }
                }
                if (dateThru == null) {
                    dateThru = CreditCard.ExtractCardDateThru(text);
                    if (dateThru != null) {
                        OcrGraphic ocrGraphic = new OcrGraphic(graphicOverlay, item);
                        graphicOverlay.add(ocrGraphic);
                    }
                }
            }
            if (card != null && dateThru != null) {
                Log.d("OcrDetectorProcessor", "Card Number detected! = " + card.getCardNumber());
                Log.d("OcrDetectorProcessor", "Card Date Thru detected! = " + dateThru.getDate());
                card.setCardDateThru(dateThru);
                graphicOverlay.post(card);
                break;
            }
        }
    }

    /**
     * Frees the resources associated with this detection processor.
     */
    @Override
    public void release() {
        graphicOverlay.clear();
        graphicOverlay = null;
    }
}
