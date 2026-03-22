import { Identifiable } from "../dto/Identifiable";
import { z } from "zod";

export class Converter {
    /**
     * Convert an aray of Identifiable to map of id and the original item
     * 
     * @note duplicate IDs are not supported, if duplicate ids were to occurr, 
     * then the first element will be overwritten
     * 
     * @param items array of items, that use the Identifiable interface
     * @returns A map of ids as keys and the original values as values
     */
    static toMap<T extends z.infer<typeof Identifiable>>(items: T[]): Map<string, T> {
        const map = new Map<string, T>();

        items.forEach((item) => {
            map.set(item.id, item);
        });

        return map;
    }
}