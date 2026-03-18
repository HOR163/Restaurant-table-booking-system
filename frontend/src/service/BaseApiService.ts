import { z } from "zod";

export abstract class BaseApiService<T> {
    // No base url due to proxy configuration in package.json
    private BASE_URL: string = "";
    private ENDPOINT_URL: string = "";

    constructor(endpoint: string, private schema: z.Schema<T>) {
        this.ENDPOINT_URL = `${this.BASE_URL}${endpoint}`;
    }

    async getAll(): Promise<T[]> {
        try {
            const response = await fetch(this.ENDPOINT_URL);
            const json = await response.json();
            return z.array(this.schema).parse(json);
        } catch (error) {
            console.error(`Error fetching data from ${this.ENDPOINT_URL}:`, error);
            return [];
        }
    }

    async create(data: Omit<T, "id">): Promise<T | null> {
        try {
            const response = await fetch(this.ENDPOINT_URL, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(data),
            });
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const json = await response.json();
            return this.schema.parse(json);
        } catch (error) {
            console.error(`Error creating data at ${this.ENDPOINT_URL}:`, error);
            return null;
        }
    }
}