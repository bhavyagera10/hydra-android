package be.ugent.zeus.hydra.data.database.minerva2;

import android.content.Context;
import android.support.annotation.NonNull;

import be.ugent.zeus.hydra.data.database.minerva2.agenda.DatabaseAgendaItemRepository;
import be.ugent.zeus.hydra.data.database.minerva2.announcement.DatabaseAnnouncementRepository;
import be.ugent.zeus.hydra.data.database.minerva2.course.DatabaseCourseRepository;
import be.ugent.zeus.hydra.data.dto.minerva.AgendaMapper;
import be.ugent.zeus.hydra.data.dto.minerva.AnnouncementMapper;
import be.ugent.zeus.hydra.data.dto.minerva.CourseMapper;
import be.ugent.zeus.hydra.domain.repository.AgendaItemRepository;
import be.ugent.zeus.hydra.domain.repository.AnnouncementRepository;
import be.ugent.zeus.hydra.domain.repository.CourseRepository;

/**
 * Provides implementations of the repository interfaces.
 *
 * @author Niko Strijbol
 */
public class RepositoryFactory {

    private static AgendaItemRepository agendaItemRepository;
    private static AnnouncementRepository announcementRepository;
    private static CourseRepository courseRepository;

    @NonNull
    public static synchronized AgendaItemRepository getAgendaItemRepository(Context context) {
        if (agendaItemRepository == null) {
            MinervaDatabase database = MinervaDatabase.get(context);
            agendaItemRepository = new DatabaseAgendaItemRepository(database.getAgendaDao(), CourseMapper.INSTANCE, AgendaMapper.INSTANCE);
        }
        return agendaItemRepository;
    }

    @NonNull
    public static synchronized AnnouncementRepository getAnnouncementRepository(Context context) {
        if (announcementRepository == null) {
            MinervaDatabase database = MinervaDatabase.get(context);
            announcementRepository = new DatabaseAnnouncementRepository(database.getAnnouncementDao(), CourseMapper.INSTANCE, AnnouncementMapper.INSTANCE);
        }
        return announcementRepository;
    }

    @NonNull
    public static synchronized CourseRepository getCourseRepository(Context context) {
        if (courseRepository == null) {
            MinervaDatabase database = MinervaDatabase.get(context);
            courseRepository = new DatabaseCourseRepository(database.getCourseDao(), CourseMapper.INSTANCE);
        }
        return courseRepository;
    }
}