import { Identifiable } from "../dto/Identifiable";
import { z } from "zod";

export class Converter {
    static toMap<T extends z.infer<typeof Identifiable>>(items: T[]): Map<string, T> {
        const map = new Map<string, T>();

        items.forEach((item) => {
            map.set(item.id, item);
        });

        return map;
    }
}