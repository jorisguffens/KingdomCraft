/*
 * This file is part of KingdomCraft.
 *
 * KingdomCraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KingdomCraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with KingdomCraft. If not, see <https://www.gnu.org/licenses/>.
 */

package com.gufli.kingdomcraft.common.placeholders;

import com.gufli.kingdomcraft.api.domain.User;
import com.gufli.kingdomcraft.api.entity.PlatformPlayer;
import com.gufli.kingdomcraft.api.placeholders.PlaceholderManager;
import com.gufli.kingdomcraft.api.placeholders.PlaceholderReplacer;
import com.gufli.kingdomcraft.common.KingdomCraftImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderManagerImpl implements PlaceholderManager {

    private final Map<String, PlaceholderReplacer> placeholderReplacers = new HashMap<>();

    public PlaceholderManagerImpl(KingdomCraftImpl kdc) {
        new DefaultPlaceholderReplacer(kdc, this);
    }

    @Override
    public void addPlaceholderReplacer(PlaceholderReplacer placeholderReplacer, String... placeholders) {
        for ( String placeholder : placeholders ) {
            placeholderReplacers.put(placeholder.toLowerCase(), placeholderReplacer);
        }
    }

    @Override
    public void removePlaceholderReplacer(PlaceholderReplacer placeholderReplacer) {
        placeholderReplacers.entrySet().removeIf(pr -> pr.getValue() == placeholderReplacer);
    }

    @Override
    public void removePlaceholderReplacer(String placeholder) {
        placeholderReplacers.remove(placeholder);
    }

    @Override
    public String handle(PlatformPlayer player, String str) {
        return handle(player, str, "");
    }

    @Override
    public String handle(PlatformPlayer player, String str, String prefix) {
        return handle(player.getUser(), str, prefix);
    }

    @Override
    public String handle(User user, String str) {
        return handle(user, str, "");
    }

    @Override
    public String handle(User user, String str, String prefix) {
        if ( str == null ) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile("(\\{[^}]+\\})");
        Matcher m = p.matcher(str);

        while ( m.find() ) {
            String placeholder = m.group(1).toLowerCase();
            placeholder = placeholder.substring(1, placeholder.length()-1); // remove brackets { and }

            if ( !placeholder.startsWith(prefix) ) {
                continue;
            }
            placeholder = placeholder.replaceFirst(Pattern.quote(prefix),"");

            PlaceholderReplacer replacer = placeholderReplacers.get(placeholder);
            if ( replacer == null ) {
                continue;
            }

            String replacement = replacer.replace(user, placeholder);
            if ( replacement == null ) {
                replacement = "";
            }

            m.appendReplacement(sb, replacement);
        }
        m.appendTail(sb);
        return sb.toString();
    }

    @Override
    public String strip(String str, String... ignore) {
        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile("(\\{[^}]+\\})");
        Matcher m = p.matcher(str);

        outer: while ( m.find() ) {
            String placeholder = m.group(1);
            placeholder = placeholder.substring(1, placeholder.length()-1);

            for ( String s : ignore ) {
                if ( placeholder.equalsIgnoreCase(s) ) {
                    m.appendReplacement(sb, "{" + placeholder + "}");
                    continue outer;
                }
            }

            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);

        return sb.toString();
    }
}
