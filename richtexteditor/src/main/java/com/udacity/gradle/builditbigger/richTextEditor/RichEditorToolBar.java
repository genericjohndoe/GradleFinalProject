package com.udacity.gradle.builditbigger.richTextEditor;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.HorizontalScrollView;
import com.udacity.gradle.builditbigger.richTextEditor.databinding.ToolbarLayoutBinding;



public class RichEditorToolBar extends HorizontalScrollView {

    RichEditor richEditor = null;

    public RichEditorToolBar(Context context){
        super(context);
        init(context);
    }

    public RichEditorToolBar(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public RichEditorToolBar(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public RichEditorToolBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ToolbarLayoutBinding bind = DataBindingUtil.inflate(inflater, R.layout.toolbar_layout, this, true);

        bind.actionUndo.setOnClickListener(view -> richEditor.undo());
        bind.actionRedo.setOnClickListener(view -> richEditor.redo());
        bind.actionBold.setOnClickListener(view -> richEditor.setBold());
        bind.actionItalic.setOnClickListener(view -> richEditor.setItalic());
        bind.actionSubscript.setOnClickListener(view -> richEditor.setSubscript());
        bind.actionSuperscript.setOnClickListener(view -> richEditor.setSuperscript());
        bind.actionStrikethrough.setOnClickListener(view -> richEditor.setStrikeThrough());
        bind.actionUnderline.setOnClickListener(view -> richEditor.setUnderline());
        bind.actionHeading1.setOnClickListener(view -> richEditor.setHeading(1));
        bind.actionHeading2.setOnClickListener(view -> richEditor.setHeading(2));
        bind.actionHeading3.setOnClickListener(view -> richEditor.setHeading(3));
        bind.actionHeading4.setOnClickListener(view -> richEditor.setHeading(4));
        bind.actionHeading5.setOnClickListener(view -> richEditor.setHeading(5));
        bind.actionHeading6.setOnClickListener(view -> richEditor.setHeading(6));
        bind.actionTxtColor.setOnClickListener(view -> {
            //todo create dialog for color picker
            //todo add hex value to method below
            //richEditor.setEditorFontColor("");
        });
        bind.actionIndent.setOnClickListener(view -> richEditor.setIndent());
        bind.actionOutdent.setOnClickListener(view -> richEditor.setOutdent());
        bind.actionAlignLeft.setOnClickListener(view -> richEditor.setAlignLeft());
        bind.actionAlignCenter.setOnClickListener(view -> richEditor.setAlignCenter());
        bind.actionAlignRight.setOnClickListener(view -> richEditor.setAlignCenter());
        bind.actionAlignFull.setOnClickListener(view -> richEditor.setAlignFull());
        bind.actionBlockquote.setOnClickListener(view ->richEditor.setBlockquote());
        bind.actionInsertBullets.setOnClickListener(view -> richEditor.setBullets());
        bind.actionInsertImage.setOnClickListener(view ->{
            //todo open dialog to either enter url or choose/take photo
            //todo pass info to following method
            //richEditor.insertImage("","");
        });
        bind.actionInsertLink.setOnClickListener(view -> {
            //todo open dialog to insert link
            //richEditor.insertLink("","");
        });
        bind.actionInsertCheckbox.setOnClickListener(view -> richEditor.insertTodo());
    }

    public void setRichEditor(RichEditor richEditor){
        this.richEditor = richEditor;
    }
}
