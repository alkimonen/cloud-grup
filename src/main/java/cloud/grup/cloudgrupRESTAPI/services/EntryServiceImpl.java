package cloud.grup.cloudgrupRESTAPI.services;

import cloud.grup.cloudgrupRESTAPI.commands.EntryForm;
import cloud.grup.cloudgrupRESTAPI.converters.EntryFormToEntry;
import cloud.grup.cloudgrupRESTAPI.domain.Entry;
import cloud.grup.cloudgrupRESTAPI.repositories.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EntryServiceImpl implements EntryService{
    private EntryRepository entryRepository;
    private EntryFormToEntry entryFormToEntry;

    @Autowired
    public EntryServiceImpl(EntryRepository entryRepository , EntryFormToEntry entryFormToEntry){
        this.entryRepository = entryRepository;
        this.entryFormToEntry = entryFormToEntry;
    }

    @Override
    public Entry getById(Long id) {
        return entryRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        entryRepository.deleteById(id);
    }

    @Override
    public Entry save(Entry entry) {
        entryRepository.save(entry);
        return entry;
    }

    @Override
    public Entry saveEntryForm(EntryForm entryform) {
        Entry entry = save(entryFormToEntry.convert(entryform));
        return entry;
    }
    @Override
    public List<Entry> listAll() {
        List<Entry> products = new ArrayList<>();
        entryRepository.findAll().forEach(products::add);
        return products;
    }
}
