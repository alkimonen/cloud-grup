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

    /*
    @RequestMapping("entry/delete/{id}")
    public void delete(@PathVariable Long id){
        if ( getURLbyID(id)) {
            entryService.delete(Long.valueOf(id));
        }
    }*/

    @RequestMapping("entry/deleteExpiredKeys")
    public void deleteExpiredKeys(){
        ArrayList<Long> expiredKeys = getExpiredKeys();
        for ( int i = 0; i < expiredKeys.size(); ++i) {
            System.out.println( "Deleting id " + expiredKeys.get(i));
            entryService.delete(expiredKeys.get(i));
        }
    }

    @RequestMapping(value = "/entry/new", method = RequestMethod.POST)
    public String savePost(@RequestBody String url) {
        Entry entry = new Entry();
        entry.setId(randomID());
        entry.setUrl(url);
        String key = random();
        while (!getURL(key).equals(""))
            key = random();

        entry.setKey(key);
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        entry.setExpirationDate(cal);

        if ( entryService.save(entry) != null)
            return "Success";
        else
            return "Failed";
    }

    @RequestMapping({"entry/new/{url}", "entry/new/{url}/{key}"})
    public String save(@PathVariable String url, @PathVariable(required = false) String key){
        Entry entry = new Entry();
        entry.setId(randomID());
        entry.setUrl(url);
        if(key == null || key.length() != 6) {
            key = random();
            while (!getURL(key).equals(""))
                key = random();
        }
        else {
            if ( !getURL(key).equals(""))
                return "Key already in use";
        }
        entry.setKey(key);
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        entry.setExpirationDate(cal);
        System.out.println(entry.toString1());

        if ( entryService.save(entry) != null)
            return "Success";
        else
            return "Failed";
    }

    private Long randomID() {
        return Long.valueOf((int)Math.random()*100000);
    }

    /*
    @RequestMapping(value="entry/{key}", method = RequestMethod.GET)
    public ModelAndView method(@PathVariable("key") String url) {
        System.out.println("DENEME YONLENDIRME");
        String original = "www.google.com";
        return new ModelAndView("redirect:" + original);
    }
    */

    @GetMapping("/deneme")
    public String deneme(){
        return "DENEME CHECK";
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public void redirect(HttpServletResponse httpServletResponse , @PathVariable String key) throws IOException {
        String original = getURL( key);
        if (original.length() > 5 && !original.substring(0,5).equals("https"))
            original = "https://" + original;
        httpServletResponse.sendRedirect(original);
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

    private String getURL( String key) {List<Entry> all = entryService.listAll();
        String original = "";
        for(int i = 0 ; i < all.size() ; i++){
            if(all.get(i).getKey().equals(key)){
                original = all.get(i).getUrl();
                break;
            }
        }
        return original;
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
