import { Attribute, AttributeSchema } from "../dto/Attribute";
import { BaseApiService } from "./BaseApiService";


export class AttributeService extends BaseApiService<Attribute> {
    constructor() {
        super(AttributeSchema);
    }

    async get(attributeId: string): Promise<Attribute | null> {
        return super.getSingle(`/attributes/${attributeId}`);
    }

    async getAll(): Promise<Attribute[]> {
        return super.getArray(`/attributes`);
    }

    async create(attribute: Omit<Attribute, "id">): Promise<Attribute | null> {
        return super.post("/attributes", attribute);
    }

    async delete(attributeId: string): Promise<number> {
        return super.delete(attributeId);
    }
}