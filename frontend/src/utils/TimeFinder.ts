import { BookingSlot } from "../dto";
import { ITime } from "../interface/ITime";

export class TimeFinder {
    // private static BOOKING_LENGTH_MINUTES: number = 180;
    private static BOOKING_TIME_STEP_MINUTES: number = 30;

    private static parseTime(time: string): ITime | null {
        const timeParts = time.split(":");
        if (timeParts.length < 2) {
            return null;
        }

        const hour = parseInt(timeParts[0]);
        const minute = parseInt(timeParts[1]);

        if (isNaN(hour) || hour > 23 || hour < 0) {
            return null;
        }
        if (isNaN(minute) || minute > 59 || minute < 0) {
            return null;
        }

        return {
            hour,
            minute
        }
    }

    /**
     * Convert time slots to booking start times, having BOOKING_TIME_STEP_MINUTES in between each start iime
     * @param slots the booking slots with start time and end time that are inclusive
     * @returns booking times with BOOKING_TIME_STEP_MINUTES time step up until the end of given slot and so for all slots given
     */
    static getStartTimes(slots: BookingSlot[]): string[] {
        const bookingTimes: string[] = [];

        for (const slot of slots) {
            const parsedStart = this.parseTime(slot.startTime);
            const parsedEnd = this.parseTime(slot.endTime);

            if (!parsedStart || !parsedEnd) {
                continue;
            }

            let currentMinutes = parsedStart.hour * 60 + parsedStart.minute;
            const endMinutes = parsedEnd.hour * 60 + parsedEnd.minute;

            if (currentMinutes > endMinutes) {
                continue;
            }

            while (currentMinutes <= endMinutes) {

                bookingTimes.push(
                    Math.floor(currentMinutes / 60).toString().padStart(2, "0") +
                    ":" + (currentMinutes % 60).toString().padStart(2, "0"));
                currentMinutes += this.BOOKING_TIME_STEP_MINUTES;
            }
        }

        return bookingTimes;
    }
}