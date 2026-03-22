import { z } from "zod";


export abstract class BaseApiService<T> {
    // No base url due to proxy
    private BASE_URL = "";

    constructor(private schema: z.Schema<T>) {
    }

    protected async getSingle(subEndpoint: string): Promise<T | null> {
        const requestUrl = `${this.BASE_URL}${subEndpoint}`;
        try {
            const response = await fetch(requestUrl);
            const json = await response.json();

            const parseResult = z.safeParse(this.schema, json);
            if (parseResult.success) {
                return json;
            }

            console.error(`Invalid schema gotten from get request to ${requestUrl}`, parseResult.error)
            return null;

        } catch (error) {
            console.error(`Error with post request to ${requestUrl} ${error}`)
            return null;
        }
    }

        protected async getArray(subEndpoint: string): Promise<T[]> {
        const requestUrl = `${this.BASE_URL}${subEndpoint}`;
        try {
            const response = await fetch(requestUrl);
            const json = await response.json();

            const parseResult = z.safeParse(z.array(this.schema), json);
            if (parseResult.success) {
                return json;
            }

            console.error(`Invalid schema gotten from get request to ${requestUrl}`, parseResult.error)
            return [];

        } catch (error) {
            console.error(`Error with get request to ${requestUrl} ${error}`)
            return [];
        }
    }

    protected async post(subEndpoint: string, data: Omit<T, "id">): Promise<T | null> {
        const requestUrl = `${this.BASE_URL}${subEndpoint}`;
        try {
            const response = await fetch(requestUrl, {
                "method": "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(data)
            });

            if (!response.ok) {
                console.error(`Post to url ${requestUrl} failed with code ${response.status}`)
                return null;
            }

            const json = await response.json();

            const parseResult = z.safeParse(this.schema, json);
            if (parseResult.success) {
                return json;
            }

            console.error(`Invalid schema gotten form post request to ${requestUrl}`, parseResult.error)
            return null;

        } catch (error) {
            console.error(`Error with post request to ${requestUrl} ${error}`)
            return null;
        }
    }

    protected async delete(subEndpoint: string): Promise<number> {
        const requestUrl = `${this.BASE_URL}${subEndpoint}`;
        try {
            const response = await fetch(requestUrl, {
                "method": "DELETE"
            });

            if (!response.ok) {
                console.error(`Delete to url ${requestUrl} failed with code ${response.status}`)
            }
            return response.status;

        } catch (error) {
            console.error(`Error with post request to ${requestUrl} ${error}`)
            return -1;
        }
    }
}