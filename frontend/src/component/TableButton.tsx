import { Button, Typography } from "@mui/material";

import { Table } from "../dto";

export function TableButton({
  table,
  isSelected,
  onSelect,
  available,
  color
}: {
  table: Table;
  isSelected: boolean;
  onSelect: (id: string) => void;
  available: boolean;
  color?: 'inherit' | 'primary' | 'secondary' | 'success' | 'error' | 'info' | 'warning';
}) {
  return (
    <Button
      sx={{ minWidth: 0}}
      variant={isSelected ? "contained" : "outlined"}
      disabled={!available}
      onClick={() => onSelect(table.id)}
      color={color ?? 'primary'}
    >
      <Typography>{table.tableNumber}</Typography>
    </Button>
  );
}
