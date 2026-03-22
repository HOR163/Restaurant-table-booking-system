import { createContext } from "react";
import { IUserContext } from "../interface";

export const userContextData: IUserContext = {
    userId: "f04b862d-eb29-4fd6-8ada-b882e71b2b26"
}

export const UserContext = createContext<IUserContext>(userContextData);
