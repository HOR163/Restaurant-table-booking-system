import { Table, TableSchema } from "../dto/Table";
import { BaseApiService } from "./BaseApiService";

export class TableService extends BaseApiService<Table> {
    constructor() {
        super(TableSchema);
    }

    async get(tableId: string): Promise<Table | null> {
        return super.getSingle(`/tables/${tableId}`);
    }

    async getRestaurantTables(restaurantId: string): Promise<Table[]> {
        return super.getArray(`/restaurants/${restaurantId}/tables`);
    }

    async create(tableData: Omit<Table, "id">): Promise<Table | null> {
        return super.post("/tables", tableData);
    }

    async delete(tableId: string): Promise<number> {
        return super.delete(`/tables/${tableId}`);
    }
}