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

package com.gufli.kingdomcraft.common.ebean.beans;

import com.gufli.kingdomcraft.api.entity.PlatformLocation;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PlatformLocationConverter implements AttributeConverter<PlatformLocation, String> {

    @Override
    public String convertToDatabaseColumn(PlatformLocation attribute) {
        return attribute.serialize();
    }

    @Override
    public PlatformLocation convertToEntityAttribute(String dbData) {
        return PlatformLocation.deserialize(dbData);
    }
}
