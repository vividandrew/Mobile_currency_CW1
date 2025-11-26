package gcu.awarno300.currency;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CurrencyExchangeManager {

    private final HashMap<String, CurrencyExchange> Library;

    public CurrencyExchangeManager()
    {
        this.Library = new HashMap<String, CurrencyExchange>();
    }

    public void add(CurrencyExchange input)
    {
        this.Library.put(input.getForeign(), input);
    }

    public CurrencyExchange get(String key)
    {
        return this.Library.get(key);
    }

    public Collection<CurrencyExchange> getListOf(String key)
    {
        if(key.length() > 0){
            return this.Library.entrySet().stream().filter(x -> x.getKey().startsWith(key))
                    .map(Map.Entry::getValue).collect(Collectors.toList());
        }
        return this.getList();
    }

    public Collection<CurrencyExchange> getList()
    {
        return this.Library.values();
    }

}
