package cloud.grup.cloudgrupRESTAPI.converters;

import cloud.grup.cloudgrupRESTAPI.commands.EntryForm;
import cloud.grup.cloudgrupRESTAPI.domain.Entry;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntryToEntryForm implements Converter<Entry, EntryForm> {
    @Override
    public EntryForm convert(Entry entry){
        EntryForm entryForm = new EntryForm();
        entryForm.setId(entry.getId());
        entryForm.setUrl(entry.getUrl());
        entryForm.setKey(entry.getKey());
        entryForm.setExpirationDate(entry.getExpirationDate());
        return entryForm;
    }
}
