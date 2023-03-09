package com.ts.code.editor.editor;

import com.ts.code.editor.R;
import com.ts.code.editor.FileUtil;
import com.ts.code.editor.SketchwareUtil;

import android.view.LayoutInflater;
import android.content.Context;
import android.widget.*;
import android.webkit.*;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceError;

import java.io.File;
import java.lang.Boolean;

public class AceEditor {
	public WebView webview;
	public AceJSInterface aceJSInterface;
    public Context c;
	public void SetUpAceCodeEditor(LinearLayout _linear,String _path,Context _c){
        c = _c;
		// clear editor
		_linear.removeAllViews();
		
		// create new webview for ace edition
		// webview = new WebView(_c);
		webview = (WebView)((LayoutInflater)_c.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.ace_code_editor,null).findViewById(R.id.CodeEditor);
		
		// enable JavaScript support
		webview.getSettings().setJavaScriptEnabled(true);
		
		// disable zoom
		// webview.getSettings().setSupportZoom(false);
		webview.getSettings().setSupportZoom(true);
		
		// allow content access
		webview.getSettings().setAllowContentAccess(true); 
		
		// allow file access
		webview.getSettings().setAllowFileAccess(true); 
		
		// setup web chrome client
		setWebChromeClient();
		
		// Add JS interface for communication between Java and JavaScript
		aceJSInterface = new AceJSInterface();
		aceJSInterface.path(_path);
		webview.addJavascriptInterface(aceJSInterface, "aceEditor");
		
		// Load editor file
		webview.loadUrl("file:/storage/emulated/0/Android/data/io.spck/files/Code editor/index.html");
		
		
		// remove webview from its parent to append in _linear
		if (((LinearLayout)webview.getParent()) != null) {
			((LinearLayout)webview.getParent()).removeView(webview);
		}
		// add sora editor to _linear
		_linear.addView(webview);
	}
	public WebView GetAceEditor(){
		return webview;
	}
	
	public void setWebChromeClient(){
		webview.setWebChromeClient(new WebChromeClient());
	}
	public class AceJSInterface{
		String path;
		public void path(String _path){
			path = _path;
		}
		@JavascriptInterface
		public String getAceEditorTheme(){
			return "monokai";
		}
		@JavascriptInterface
		public String getCode(){
			return FileUtil.readFile(path);
		}
		@JavascriptInterface
		public String getPath(){
			return path;
		}
		@JavascriptInterface
		public void saveFile(String code){
			if (new File(path).canWrite()) {
				FileUtil.writeFile(path, code);
                SketchwareUtil.showMessage(c, "File will be saved.");
			}
			else {
				SketchwareUtil.showMessage(c, "Path can not be modified.");
			}
			
		}
	}
    public void saveFile(){
        webview.loadUrl("javascript:saveFile()");
    }
}
