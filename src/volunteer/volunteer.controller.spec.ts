import { Test, TestingModule } from '@nestjs/testing';
import { VolunteerController } from './volunteer.controller';
import { VolunteerService } from './volunteer.service';
import { CreateVolunteerDto } from './dto/create-volunteer.dto';
import { UpdateVolunteerDto } from './dto/update-volunteer.dto';

describe('VolunteerController', () => {
  let controller: VolunteerController;
  let service: VolunteerService;

  const mockVolunteer = {
    id: 1,
    name: 'John Doe',
    email: 'john@example.com',
    phone: '1234567890',
    availability: 'weekends',
    skills: ['dog walking', 'training'],
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  const mockVolunteerService = {
    create: jest.fn(),
    findAll: jest.fn(),
    findOne: jest.fn(),
    update: jest.fn(),
    remove: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [VolunteerController],
      providers: [
        {
          provide: VolunteerService,
          useValue: mockVolunteerService,
        },
      ],
    }).compile();

    controller = module.get<VolunteerController>(VolunteerController);
    service = module.get<VolunteerService>(VolunteerService);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should be defined', () => {
    expect(controller).toBeDefined();
  });

  describe('create', () => {
    it('should create a new volunteer', async () => {
      const createDto: CreateVolunteerDto = {
        name: 'John Doe',
        email: 'john@example.com',
        phone: '1234567890',
        availability: 'weekends',
        skills: ['dog walking', 'training'],
      };

      mockVolunteerService.create.mockResolvedValue(mockVolunteer);

      const result = await controller.create(createDto);

      expect(result).toEqual(mockVolunteer);
      expect(service.create).toHaveBeenCalledWith(createDto);
      expect(service.create).toHaveBeenCalledTimes(1);
    });

    it('should throw an error if creation fails', async () => {
      const createDto: CreateVolunteerDto = {
        name: 'John Doe',
        email: 'john@example.com',
        phone: '1234567890',
        availability: 'weekends',
        skills: ['dog walking'],
      };

      mockVolunteerService.create.mockRejectedValue(new Error('Creation failed'));

      await expect(controller.create(createDto)).rejects.toThrow('Creation failed');
    });
  });

  describe('findAll', () => {
    it('should return an array of volunteers', async () => {
      const volunteers = [mockVolunteer, { ...mockVolunteer, id: 2, name: 'Jane Doe' }];
      mockVolunteerService.findAll.mockResolvedValue(volunteers);

      const result = await controller.findAll();

      expect(result).toEqual(volunteers);
      expect(service.findAll).toHaveBeenCalledTimes(1);
    });

    it('should return an empty array if no volunteers exist', async () => {
      mockVolunteerService.findAll.mockResolvedValue([]);

      const result = await controller.findAll();

      expect(result).toEqual([]);
      expect(service.findAll).toHaveBeenCalledTimes(1);
    });
  });

  describe('findOne', () => {
    it('should return a volunteer by id', async () => {
      mockVolunteerService.findOne.mockResolvedValue(mockVolunteer);

      const result = await controller.findOne('1');

      expect(result).toEqual(mockVolunteer);
      expect(service.findOne).toHaveBeenCalledWith(1);
      expect(service.findOne).toHaveBeenCalledTimes(1);
    });

    it('should throw an error if volunteer not found', async () => {
      mockVolunteerService.findOne.mockRejectedValue(new Error('Volunteer not found'));

      await expect(controller.findOne('999')).rejects.toThrow('Volunteer not found');
    });
  });

  describe('update', () => {
    it('should update a volunteer', async () => {
      const updateDto: UpdateVolunteerDto = {
        name: 'John Updated',
        availability: 'weekdays',
      };

      const updatedVolunteer = { ...mockVolunteer, ...updateDto };
      mockVolunteerService.update.mockResolvedValue(updatedVolunteer);

      const result = await controller.update('1', updateDto);

      expect(result).toEqual(updatedVolunteer);
      expect(service.update).toHaveBeenCalledWith(1, updateDto);
      expect(service.update).toHaveBeenCalledTimes(1);
    });

    it('should throw an error if update fails', async () => {
      const updateDto: UpdateVolunteerDto = { name: 'John Updated' };
      mockVolunteerService.update.mockRejectedValue(new Error('Update failed'));

      await expect(controller.update('1', updateDto)).rejects.toThrow('Update failed');
    });
  });

  describe('remove', () => {
    it('should remove a volunteer', async () => {
      mockVolunteerService.remove.mockResolvedValue({ deleted: true });

      const result = await controller.remove('1');

      expect(result).toEqual({ deleted: true });
      expect(service.remove).toHaveBeenCalledWith(1);
      expect(service.remove).toHaveBeenCalledTimes(1);
    });

    it('should throw an error if removal fails', async () => {
      mockVolunteerService.remove.mockRejectedValue(new Error('Removal failed'));

      await expect(controller.remove('1')).rejects.toThrow('Removal failed');
    });
  });
});
