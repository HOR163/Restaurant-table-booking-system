import { Chip } from "@mui/material";

export function AttributeCard({
  name,
  isSelected,
  isMissing,
}: {
  name: string;
  isSelected: boolean;
  isMissing: boolean;
}) {
  return (
    <Chip
      label={name}
      variant="outlined"
      color={isSelected ? "primary" : "default"}
    />
  );
}
