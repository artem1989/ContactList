package ua.test.apps.contactlist;

import com.itera.contactlist.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddContactActivity extends Activity {
	
	private EditText mName;
	private EditText mPhone;
	private Button mButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);		
		mName = (EditText) findViewById(R.id.add_name);
		mPhone = (EditText) findViewById(R.id.add_phone);
		mButton = (Button) findViewById(R.id.submit);
		mButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				createNewContact(mName.getText().toString(), mPhone.getText().toString());				
			}
		});
	}
	
	private void createNewContact(String name, String phoneNumber) {
		if(TextUtils.isEmpty(name) || TextUtils.isEmpty(phoneNumber)){
			Toast.makeText(this, "Please fill in name and phone number!", Toast.LENGTH_LONG).show();
		} else {
			createContact(name, phoneNumber);
			setResult(RESULT_OK, getIntent());
		    finish();
		}
	}
	
	private void createContact(String name, String phone){
		try {
            ContentResolver cr = this.getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, name);
            cv.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
            cv.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            cr.insert(ContactsContract.RawContacts.CONTENT_URI, cv);         
        }
        catch(Exception e){
        	Toast.makeText(this, "Contact could not be added", Toast.LENGTH_LONG).show();
        }
	}
}
