package cloud.grup.cloudgrupRESTAPI.controllers;

import cloud.grup.cloudgrupRESTAPI.converters.EntryToEntryForm;
import cloud.grup.cloudgrupRESTAPI.domain.Entry;
import cloud.grup.cloudgrupRESTAPI.services.EntryService;
import cloud.grup.cloudgrupRESTAPI.services.EntryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
public class EntryController {
    private EntryService entryService;
    private EntryToEntryForm entryToEntryForm;

    @Autowired
    public void setEntryToEntryForm(EntryToEntryForm entryToEntryForm){
        this.entryToEntryForm = entryToEntryForm;
    }
    @Autowired
    public void setEntryService(EntryService entryService){
        this.entryService = entryService;
    }

    @RequestMapping(value = {"/entry", "/entry/{key}", "/entry/{key}/{burl}"})//, method = RequestMethod.POST)
    public String newEntry(@PathVariable(required = false) String key, @PathVariable(required = false) String burl, @RequestBody(required = false) String url) {
        Entry entry = new Entry();

        if (key != null && burl != null) {
            entry.setUrl(burl);
        }
        else if ( url != null)
            entry.setUrl(url);
        else
            return "Url needed";

        if (key == null || key.length() != 6) {
            key = random();
            while (!getURL(key).equals(""))
                key = random();
        }
        else {
            if ( !getURL(key).equals(""))
                return "Key already in use";
        }
        entry.setKey(key);

        entry.initExpirationDate();
        /*
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);
        entry.setExpirationDate(cal);*/

        entry.initAccessRate();

        if ( entryService.save(entry) != null)
            return "cloud-grup.herokuapp.com/" + key;
        else
            return "Task failed";
    }

    @GetMapping("/entry/info/{key}")
    public String entryAnalytics(@PathVariable String key)  throws IOException {
        Entry e = getEntry( key);
        if ( e == null)
            return "Entry not found";
        return "cloud-grup.herokuapp.com/" + e.getKey() + " | " + e.getUrl()
                + " | " + e.getExpirationDate().getTime() + " | Accessed " + e.getAccessRate() + " times.";
    }

    @RequestMapping("/entry/deleteExpiredKeys")
    public String deleteExpiredKeys(){
        String deleted = "| Expiration Date:             | Key    | Original URL\n";

        Calendar today = Calendar.getInstance();
        today.setTime(new Date());

        List<Entry> all = entryService.listAll();
        for(int i = 0 ; i < all.size() ; i++) {
            Calendar expDate = all.get(i).getExpirationDate();
            if ( expDate.compareTo(today) < 0) {
                entryService.delete(all.get(i).getId());
                deleted += "| " + all.get(i).getExpirationDate().getTime();
                deleted += " | " + all.get(i).getKey();
                deleted += " | " + all.get(i).getUrl();
                deleted += " | Accessed " + all.get(i).getAccessRate() + " times.\n";
            }
        }
        return deleted;
    }

    private Long randomID() {
        return Long.valueOf((int)Math.random()*10000);
    }

    @GetMapping("/deneme")
    public String rolled(HttpServletResponse httpServletResponse)  throws IOException {
        try {
            httpServletResponse.sendRedirect("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        }
        catch (IOException e) {};
        return "Never gonna give you up\n";
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public void redirect(HttpServletResponse httpServletResponse , @PathVariable String key) throws IOException {
        String original = getURL( key);
        if (original.length() > 5 && !original.substring(0,5).equals("https"))
            original = "https://" + original;
        try {
            httpServletResponse.sendRedirect(original);
        }
        catch (IOException e) {
            return;
        };
    }

    private String random() {
        String ret = "";
        for(int i = 0 ; i < 6 ; i++){
            int option = (int) (Math.random()*3);

            if ( option == 0)
                ret += (char)((Math.random()*26)+'a');
            else if ( option == 1)
                ret += (char)((Math.random()*26)+'A');
            else
                ret += (char)((Math.random()*10)+'0');
        }
        return ret;
    }

    private Entry getEntry( String key) {
        List<Entry> all = entryService.listAll();
        for(int i = 0 ; i < all.size() ; i++){
            if(all.get(i).getKey().equals(key)){
                return all.get(i);
            }
        }
        return null;
    }

    private String getURL( String key) {
        Entry e = getEntry( key);
        if ( e != null) {
            entryService.save(e);
            return e.getUrl();
        }
        else
            return "";
    }
    private boolean getURLbyID( Long id) {
        List<Entry> all = entryService.listAll();
        for(int i = 0 ; i < all.size() ; i++){
            if(all.get(i).getId() == id){
                return true;
            }
        }
        return false;
    }

    private ArrayList<Long> getExpiredKeys() {
        ArrayList<Long> expired = new ArrayList<Long>();

        Date date = new Date();
        Calendar today = Calendar.getInstance();
        today.setTime(date);

        List<Entry> all = entryService.listAll();
        for(int i = 0 ; i < all.size() ; i++) {
            Calendar expDate = all.get(i).getExpirationDate();
            if ( expDate.compareTo(today) < 0)
                expired.add(all.get(i).getId());
        }
        return expired;
    }

}

