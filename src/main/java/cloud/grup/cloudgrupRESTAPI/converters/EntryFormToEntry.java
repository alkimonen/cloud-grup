package cloud.grup.cloudgrupRESTAPI.converters;

import cloud.grup.cloudgrupRESTAPI.commands.EntryForm;
import cloud.grup.cloudgrupRESTAPI.domain.Entry;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class EntryFormToEntry implements Converter<EntryForm , Entry> {
    @Override
    public Entry convert(EntryForm entryForm){
        Entry entry = new Entry();
        if (entryForm.getId() != null  && !StringUtils.isEmpty(entryForm.getId())) {
            entry.setId(new Long(entryForm.getId()));
        }
        entry.setUrl(entryForm.getUrl());
        entry.setKey(entryForm.getKey());
        entry.setExpirationDate(entryForm.getExpirationDate());
        return entry;
    }
}
